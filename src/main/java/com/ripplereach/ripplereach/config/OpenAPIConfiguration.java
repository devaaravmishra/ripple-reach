package com.ripplereach.ripplereach.config;

import com.ripplereach.ripplereach.constants.Messages;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
public class OpenAPIConfiguration {

  @Value("${application.version}")
  private String projectVersion;

  @Bean
  public OpenAPI rippleReachAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Ripple Reach API")
                .description(Messages.PLATFORM_DESCRIPTION)
                .version(projectVersion)
                .license(new License().name("MIT")));
  }
}
