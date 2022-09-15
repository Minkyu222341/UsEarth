package sparta.seed.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.sercurity.UserDetailsImpl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SseController {

	public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@CrossOrigin
	@GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
	public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		// 토큰에서 user의 pk값 파싱
		if (userDetails != null) {
			Long userId = userDetails.getId();

			// 현재 클라이언트를 위한 SseEmitter 생성
			SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
			try {
				// 연결!!
				sseEmitter.send(SseEmitter.event().name("connect"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// user의 pk값을 key값으로 해서 SseEmitter를 저장
			sseEmitters.put(userId, sseEmitter);

			sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
			sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
			sseEmitter.onError((e) -> sseEmitters.remove(userId));

			return sseEmitter;
		}else throw new CustomException(ErrorCode.UNKNOWN_ERROR);
	}
}
