package sparta.seed.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.member.domain.Authority;
import sparta.seed.member.domain.Member;
import sparta.seed.sercurity.UserDetailsImpl;
import sparta.seed.util.RedisService;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 ;  // 1일
  private static final String MEMBER_USERNAME = "memberUsername";
  private static final String MEMBER_NICKNAME = "memberNickname";
  private static final String MEMBER_ID = "memberId";
  private final RedisService redisService;
  private Authority authority;


  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey, RedisService redisService) {
    this.redisService = redisService;
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(String memberId,String memberNickname, String memberAuthority) {
    long now = (new Date()).getTime();
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
            .setSubject(memberId)
            .claim(MEMBER_NICKNAME,memberNickname)
            .claim(AUTHORITIES_KEY,memberAuthority)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    return accessToken;
  }

  public String generateRefreshToken(String memberId) {
    long now = (new Date()).getTime();
    String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .claim(MEMBER_ID,memberId)
            .signWith(key, SignatureAlgorithm.HS512)
            .setHeaderParam("JWT_HEADER_PARAM_TYPE", "headerType")
            .compact();
    redisService.setValues(memberId,refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));
    return refreshToken;
  }



  public Authentication getAuthentication(String accessToken) {

    Claims claims = parseClaims(accessToken);
    if (Authority.ROLE_USER.toString().equals(claims.get(AUTHORITIES_KEY))) {
      authority = Authority.ROLE_USER;
    } else {
      authority = Authority.ROLE_ADMIN;
    }

    Member member = Member.builder()
            .username((String) claims.get(MEMBER_USERNAME))
            .nickname((String) claims.get(MEMBER_NICKNAME))
            .authority(authority)
            .id(Long.valueOf(claims.getSubject()))
            .build();

    UserDetails principal = new UserDetailsImpl(member);
    return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      throw new MalformedJwtException("잘못된 JWT 서명입니다.");
    } catch (UnsupportedJwtException e) {
      throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(Jwts.header(), Jwts.claims(), ErrorCode.EXPIRED_TOKEN.getMsg());
    }
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public void validateHttpHeader(HttpServletRequest servletRequest){
    String authorization = servletRequest.getHeader("Authorization");
    try {
      if (!(authorization == null || authorization.equals("undefined"))) {
        String token = authorization.substring(BEARER_TYPE.length());
        validateToken(token);
      }
    } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
      throw new CustomException(ErrorCode.WRONG_TYPE_TOKEN);
    } catch(UnsupportedJwtException e){
      throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
    } catch(IllegalArgumentException e){
      throw new CustomException(ErrorCode.BE_NOT_VALID_TOKEN);
    } catch(ExpiredJwtException e){
      throw new CustomException(ErrorCode.EXPIRED_TOKEN);
    }
  }
}