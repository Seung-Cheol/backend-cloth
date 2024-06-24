package project.store.member.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.store.common.util.RedisUtil;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final RedisUtil redisUtil;

  private Long EXPIRATION_MILLISECONDS = 1000 * 60 * 5L;

  private Long REFRESH_EXPIRATION_MILLISECONDS = 1000 * 60 * 24 * 30L;

  @Value("${jwt.secret}")
  private String secretKey;
  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public TokenInfo createToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));
    Date now = new Date();
    Date accessExpiration = new Date(now.getTime() + EXPIRATION_MILLISECONDS);
    Date refreshExpiration = new Date(now.getTime() + REFRESH_EXPIRATION_MILLISECONDS);
    CustomMember customUser = (CustomMember) authentication.getPrincipal();

    String accessToken = Jwts
      .builder()
      .subject(authentication.getName())
      .claim("auth",authorities)
      .claim("id",customUser.getId())
      .issuedAt(now)
      .expiration(accessExpiration)
      .signWith(key)
      .compact();

    String refreshToken = Jwts
      .builder()
      .subject(authentication.getName())
      .claim("auth",authorities)
      .claim("id",customUser.getId())
      .claim("refresh",true)
      .issuedAt(now)
      .expiration(refreshExpiration)
      .signWith(key)
      .compact();
    redisUtil.setListData(customUser.getEmail(),refreshToken, 30);

    return new TokenInfo("Bearer", accessToken, refreshToken);
  }

  public Authentication getAuthentication(String token) {
      Claims claims =getClaims(token);
      Object auth = claims.get("auth");
      Long id = Long.parseLong(claims.get("id").toString());
      Collection<SimpleGrantedAuthority> authorities = Arrays.stream(String.valueOf(auth).split(","))
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
      UserDetails principal = new CustomMember(id,claims.getSubject(),"",authorities);
      return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }


  private Claims getClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  public TokenInfo createTokenFromRefreshToken(String refreshToken) {
    Claims claims = getClaims(refreshToken);
    String subject = claims.getSubject();
    String authorities = (String) claims.get("auth");
    Long id = Long.parseLong(claims.get("id").toString());
    List<String> data =  redisUtil.getListData(subject);

    if(data == null || !data.contains(refreshToken)) {
      throw new SecurityException("Invalid refresh token");
    }

    Date now = new Date();
    Date accessExpiration = new Date(now.getTime() + EXPIRATION_MILLISECONDS);
    Date refreshExpiration = new Date(now.getTime() + REFRESH_EXPIRATION_MILLISECONDS);

    String accessToken = Jwts
      .builder()
      .subject(subject)
      .claim("id", id)
      .claim("auth", authorities)
      .issuedAt(now)
      .expiration(accessExpiration)
      .signWith(key)
      .compact();

    String newRefreshToken = Jwts
      .builder()
      .subject(subject)
      .claim("id",id)
      .claim("auth", authorities)
      .claim("refresh",true)
      .issuedAt(now)
      .expiration(refreshExpiration)
      .signWith(key)
      .compact();
    redisUtil.setListData(subject,refreshToken, 30);
    redisUtil.deleteListData(subject,refreshToken,30);

    return new TokenInfo("Bearer", accessToken, newRefreshToken);
  }

  public TokenInfo refreshToken(String refreshToken) {
    if (validateToken(refreshToken)) {
      return createTokenFromRefreshToken(refreshToken);
    } else {
      return null;
    }
  }

  public boolean isRefreshToken(String token) {
    Claims claims = getClaims(token);
    return claims.containsKey("refresh");
  }
}
