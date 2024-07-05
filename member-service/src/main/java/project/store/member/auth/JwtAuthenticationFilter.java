//package project.store.member.auth;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//import project.store.member.auth.JwtTokenProvider;
//
//public class JwtAuthenticationFilter extends GenericFilterBean {
//  private final JwtTokenProvider jwtTokenProvider;
//
//  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
//    this.jwtTokenProvider = jwtTokenProvider;
//  }
//
//  @Override
//  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//    throws IOException, ServletException {
//    try {
//      String token = resolveToken((HttpServletRequest) request);
//
//      if (
//        token != null &&
//          jwtTokenProvider.validateToken(token) &&
//          !jwtTokenProvider.isRefreshToken(token)
//      ) {
//        Authentication authentication = jwtTokenProvider.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//      } else {
//        throw new SecurityException("Invalid or missing token");
//      }
//  } catch (SecurityException e) {
//    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//  }
//    chain.doFilter(request,response);
//  }
//
//  private String resolveToken(HttpServletRequest request) {
//    String bearerToken = request.getHeader("Authorization");
//    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
//      return bearerToken.substring(7);
//    } else {
//      return null;
//    }
//  }
//  private String resolveRefreshToken(HttpServletRequest request) {
//    String bearerToken = request.getHeader("RefreshToken");
//    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
//      return bearerToken.substring(7);
//    } else {
//      return null;
//    }
//  }
//
//}
