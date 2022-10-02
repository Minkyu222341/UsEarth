package sparta.seed.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sparta.seed.community.domain.dto.requestdto.ProofRequestDto;
import sparta.seed.community.domain.dto.responsedto.ProofCountResponseDto;
import sparta.seed.community.domain.dto.responsedto.ProofHeartResponseDto;
import sparta.seed.community.domain.dto.responsedto.ProofResponseDto;
import sparta.seed.community.service.ProofService;
import sparta.seed.login.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProofController {
	private final ProofService proofService;

  /**
   * 글에 달린 인증글 전체 조회
   */
  @GetMapping("/api/community/{communityId}/proof")

	public List<ProofResponseDto> getAllProof(@PathVariable Long communityId,
																						@RequestParam("page") int page,
																						@RequestParam("size") int size,
																						@AuthenticationPrincipal UserDetailsImpl userDetails,
																						HttpServletRequest servletRequest){

			return proofService.getAllProof(communityId, page, size, userDetails, servletRequest);
	}

	/**
	 * 글에 달린 인증글 상세 조회
	 */
	@GetMapping("/api/proof/{proofId}")
	public ProofResponseDto getProof(@PathVariable Long proofId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest servletRequest){
		return proofService.getProof(proofId, userDetails, servletRequest);
	}

  /**
   * 인증글 작성
   */
	@PostMapping(value = "/api/community/{communityId}/proof", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> createProof(@PathVariable Long communityId,
																											@Valid @RequestPart(value = "dto") ProofRequestDto proofRequestDto,
																											@RequestPart List<MultipartFile> multipartFile,
																											@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException, ParseException {

		return proofService.createProof(communityId, proofRequestDto, multipartFile, userDetails);
	}

  /**
   * 인증글 수정
   */
	@PatchMapping(value = "/api/proof/{proofId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> updateProof(@PathVariable Long proofId,
	                                    @Valid @RequestPart(value = "dto") ProofRequestDto proofRequestDto,
	                                    @RequestPart(required = false) List<MultipartFile> multipartFile,
	                                    @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
		return proofService.updateProof(proofId, proofRequestDto, multipartFile, userDetails);
	}

  /**
   * 인증글 삭제
   */
	@DeleteMapping("/api/proof/{proofId}")
	public ResponseEntity<Boolean> deleteProof(@PathVariable Long proofId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		return proofService.deleteProof(proofId, userDetails);
	}

	/**
	 * 인증글 댓글 , 좋아요 갯수 조회
	 */
	@GetMapping("/api/proof/count/{proofId}")
	public ProofCountResponseDto countProof(@PathVariable Long proofId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest servletRequest){
		return proofService.countProof(proofId, userDetails, servletRequest);
	}

	/**
	 * 인증글 좋아요
	 */
	@PatchMapping("/api/proof/heart/{proofId}")
	public ProofHeartResponseDto heartProof(@PathVariable Long proofId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		return proofService.heartProof(proofId, userDetails);
	}
}
