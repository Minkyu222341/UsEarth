package sparta.seed.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sparta.seed.community.domain.Proof;
import sparta.seed.community.repository.ProofRepository;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;

import static sparta.seed.sse.SseController.sseEmitters;

@RequiredArgsConstructor
@Service
public class NotificationService {

	private final ProofRepository proofRepository;

	public void notifyAddCommentEvent(Long proofId) {
		// 댓글에 대한 처리 후 해당 댓글이 달린 게시글의 pk값으로 게시글을 조회
		Proof proof = proofRepository.findById(proofId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROOF));

		Long userId = proof.getMemberId();

		if (sseEmitters.containsKey(userId)) {
			SseEmitter sseEmitter = sseEmitters.get(userId);
			try {
				sseEmitter.send(SseEmitter.event().name("addComment").data("내가 작성한 인증글에 댓글이 달렸어요!"));
			} catch (Exception e) {sseEmitters.remove(userId);}
		}
	}
}
