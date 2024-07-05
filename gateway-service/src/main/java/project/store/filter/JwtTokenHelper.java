package project.store.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import project.store.CustomMember;

@Component
public class JwtTokenHelper {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
  }

  // Validate the JWT token
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public CustomMember extractPayloadFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    String id = String.valueOf(claims.get("id"));
    Object auth = claims.get("auth");
    System.out.print(auth);
    Collection<SimpleGrantedAuthority> authorities = Arrays.stream(String.valueOf(auth).split(","))
    .map(SimpleGrantedAuthority::new)
    .collect(Collectors.toList());
    return new CustomMember(id, claims.getSubject(),"", authorities);
  }

  public Claims getAllClaimsFromToken(String token) {
    try {
      return Jwts.parser().verifyWith(key)
        .build().parseSignedClaims(token).getPayload();
    } catch (ExpiredJwtException | MalformedJwtException e) {
      //throw new ApiException("Invalid Token: " + e.getLocalizedMessage());
      System.out.println("Invalid Token: " + e.getLocalizedMessage());
      return null;
    }
  }
}