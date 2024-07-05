package project.store.filter;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import project.store.CustomMember;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

  private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenHelper jwtTokenHelper;

  public JwtAuthenticationFilter(JwtTokenHelper jwtTokenHelper) {
    this.jwtTokenHelper = jwtTokenHelper;
  }

  private final String AUTHORIZATION = "Authorization";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String requestToken = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);

    String token = null;

    if (requestToken != null && requestToken.startsWith("Bearer")) {
      token = requestToken.substring(7).trim();
      if (jwtTokenHelper.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {

        CustomMember user = jwtTokenHelper.extractPayloadFromToken(token);
        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
          .map((role) -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          user,  null, authorities);

        SecurityContext context = new SecurityContextImpl(authenticationToken);

        // Set the Principal header in the request
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
          .header("id", user.getId())
          .build();

        ServerWebExchange modifiedExchange = exchange.mutate()
          .request(modifiedRequest)
          .build();

        return chain.filter(modifiedExchange).contextWrite(
          ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
      } else {
        LOGGER.error("TOKEN IS MALFORMED OR EXPIRED");
      }
    } else {
      LOGGER.error("TOKEN NOT FOUND");
    }
    return chain.filter(exchange);
  }

}