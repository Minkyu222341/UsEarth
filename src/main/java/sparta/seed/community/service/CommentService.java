package sparta.seed.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.seed.community.domain.Comment;
import sparta.seed.community.domain.Proof;
import sparta.seed.community.domain.dto.requestdto.CommentRequestDto;
import sparta.seed.community.domain.dto.responsedto.CommentResponseDto;
import sparta.seed.community.domain.dto.responsedto.CommentResponseListDto;
import sparta.seed.community.repository.CommentRepository;
import sparta.seed.community.repository.ParticipantsRepository;
import sparta.seed.community.repository.ProofRepository;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.jwt.TokenProvider;
import sparta.seed.msg.ResponseMsg;
import sparta.seed.s3.S3Uploader;
import sparta.seed.sercurity.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final ProofRepository proofRepository;
	private final CommentRepository commentRepository;
	private final S3Uploader s3Uploader;
	private final ParticipantsRepository participantsRepository;
	private final TokenProvider tokenProvider;
	private final CommunityService communityService;
	private final SlangService slangService;


	/**
	 * 댓글 조회
	 */
	public CommentResponseListDto getAllComment(Long proofId, UserDetailsImpl userDetails, HttpServletRequest servletRequest) {

		tokenProvider.validateHttpHeader(servletRequest);

		try {
			List<Comment> commentList = commentRepository.findAllByProof_Id(proofId);
			CommentResponseListDto commentResponseListDtoList = new CommentResponseListDto(proofId,
					communityService.getDateStatus(proofRepository.findById(proofId).get().getCommunity()));

			for (Comment comment : commentList) {
				commentResponseListDtoList.addCommentResponseDto(CommentResponseDto.builder()
						.commentId(comment.getId())
						.creatAt(comment.getCreatedAt())
						.nickname(comment.getNickname())
						.content(comment.getContent())
						.img(comment.getImg())
						.writer(userDetails != null && comment.getMemberId().equals(userDetails.getId()))
						.build());
			}
			return commentResponseListDtoList;
		}catch (Exception e) {throw new CustomException(ErrorCode.NOT_FOUND_COMMENT);}
	}

	/**
	 * 댓글작성
	 */
	public ResponseEntity<CommentResponseDto> createComment(Long proofId, CommentRequestDto commentRequestDto,
	                                        MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
		Proof proof = proofRepository.findById(proofId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROOF));
		if (!participantsRepository.existsByCommunityAndMemberId(proof.getCommunity(), userDetails.getId())) {
			throw new CustomException(ErrorCode.NOT_PARTICIPATED);
		}
		String nickname = isChangedNickname(commentRequestDto, userDetails);

		slangService.checkSlang(commentRequestDto.getContent());

		Comment comment = Comment.builder()
						.memberId(userDetails.getId())
						.nickname(nickname)
						.content(commentRequestDto.getContent())
						.img(returnImageUrl(multipartFile))
						.proof(proof)
						.build();
				proof.addComment(comment);

				commentRepository.save(comment);
		return ResponseEntity.ok().body(CommentResponseDto.builder()
						.commentId(comment.getId())
						.content(comment.getContent())
						.creatAt(comment.getCreatedAt())
						.img(comment.getImg())
						.nickname(nickname)
						.writer(true)
						.build());
	}

	/**
	 * 댓글 수정
	 */
	@Transactional
	public ResponseEntity<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto,
																													MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
		String nickname = isChangedNickname(commentRequestDto, userDetails);

		if (userDetails != null && comment.getMemberId().equals(userDetails.getId())) {
			slangService.checkSlang(commentRequestDto.getContent());
			comment.update(commentRequestDto.getContent(),nickname);

			if (commentRequestDto.isDelete() | multipartFile != null) {
				comment.setImg(returnImageUrl(multipartFile));
			}

			return ResponseEntity.ok().body(CommentResponseDto.builder()
							.commentId(comment.getId())
							.content(comment.getContent())
							.creatAt(comment.getCreatedAt())
							.img(comment.getImg())
							.nickname(nickname)
							.writer(true)
							.build());
		}
		throw new CustomException(ErrorCode.INCORRECT_USERID);
	}

	/**
	 * 댓글 삭제
	 */
	public ResponseEntity<Boolean> deleteComment(Long commentId, UserDetailsImpl userDetails) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
		Proof proof = proofRepository.findById(comment.getProof().getId())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROOF));

		if(userDetails !=null && comment.getMemberId().equals(userDetails.getId())){
			proof.removeComment(comment);
			commentRepository.delete(comment);
			return ResponseEntity.ok().body(true);
		}throw new CustomException(ErrorCode.INCORRECT_USERID);
	}

	private String returnImageUrl(MultipartFile multipartFile) throws IOException {
		if (multipartFile != null) {
			return s3Uploader.upload(multipartFile).getUploadImageUrl();
		}else return null;
	}

	private String isChangedNickname(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
		String nickname;
		if(commentRequestDto.getChangeNickname()!=null){
			return nickname = commentRequestDto.getChangeNickname();
		}else{
			return nickname =userDetails.getNickname();
		}
	}
}
