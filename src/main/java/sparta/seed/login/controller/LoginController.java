package sparta.seed.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.seed.login.domain.dto.responsedto.TokenResponseDto;
import sparta.seed.login.service.GoogleUserService;
import sparta.seed.login.service.KakaoUserService;
import sparta.seed.login.service.NaverUserService;
import sparta.seed.member.service.MemberService;
import sparta.seed.sercurity.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class LoginController {
  private final GoogleUserService googleUserService;
  private final KakaoUserService kakaoUserService;
  private final NaverUserService naverUserService;
  private final MemberService memberService;

  /**
   * 카카오 로그인
   */
  @GetMapping("/user/kakao/callback")
  public TokenResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return kakaoUserService.kakaoLogin(code, response);
  }

  /**
   * 구글 로그인
   */
  @GetMapping("/user/google/callback")
  public TokenResponseDto googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return googleUserService.googleLogin(code, response);
  }

  /**
   * 네이버 로그인
   */
  @GetMapping("/user/naver/callback")
  public TokenResponseDto naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
    return naverUserService.naverLogin(code, state, response);
  }

  /**
   * 로그아웃
   */
  @GetMapping("/user/logout")
  public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return memberService.logout(userDetails);
  }

  /**
   * 리프레쉬토큰
   */
  @GetMapping("/user/reissue")  //재발급을 위한 로직
  public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
    return memberService.reissue(request, response);
  }
  /**
   헬스체크 컨트롤러
   */
  @GetMapping("/user/health")
  public String checkHealth() {
    return "healthyV2";
  }
}
