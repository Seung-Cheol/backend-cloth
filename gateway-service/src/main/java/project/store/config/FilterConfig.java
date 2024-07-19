package project.store.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import project.store.filter.JwtAuthenticationFilter;

@Configuration
public class FilterConfig {
  Environment env;

  public FilterConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter authFilter) {

    return builder.routes()
      .route(r -> r.path("/member/**")
        .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
          .addResponseHeader("first-response", "first-response-header-from-java")
        )
        .uri("http://localhost:8081"))
      .route(r -> r.path("/order/**")
        .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
          .addResponseHeader("first-response", "first-response-header-from-java")
        )
        .uri("http://localhost:8085"))
      .route(r -> r.path("/cloth/**")
        .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
          .addResponseHeader("first-response", "first-response-header-from-java")
        )
        .uri("http://localhost:8083"))
      .route(r -> r.path("/payment/**")
        .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
          .addResponseHeader("first-response", "first-response-header-from-java")
        )
        .uri("http://localhost:8084"))
      .build();
  }

}