package project.store.member.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64.Decoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private Long EXPIRATION_MILLISECONDS = 1000 * 60 * 15L;


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

    String accessToken = Jwts
      .builder()
      .subject(authentication.getName())
      .claim("auth",authorities)
      .issuedAt(now)
      .expiration(accessExpiration)
      .signWith(key)
      .compact();
    return new TokenInfo("Bearer", accessToken);
  }

  public Authentication getAuthentication(String token) {
    Claims claims =getClaims(token);
      Object auth = claims.get("auth");
      Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) Arrays.stream(String.valueOf(auth).split(",")).map(
        SimpleGrantedAuthority::new);
      UserDetails principal = new User(claims.getSubject(), "", authorities);
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
}
