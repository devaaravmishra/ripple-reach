package com.ripplereach.ripplereach.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfiguration {

  @Bean
  public OpenAPI rippleReachAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Ripple Reach API")
                .description(
                    "Conveying the idea of creating waves of influence and connection that extend"
                        + " far and wide.")
                .version("v0.0.1")
                .license(new License().name("MIT")));
  }
}
