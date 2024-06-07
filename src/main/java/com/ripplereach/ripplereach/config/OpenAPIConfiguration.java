package com.ripplereach.ripplereach.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

  @Bean
  public OpenAPI expenseAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Ripple Reach")
                .description(
                    "Conveying the idea of creating waves of influence and connection that extend"
                        + " far and wide.")
                .version("v0.0.1")
                .license(new License().name("MIT")));
  }
}
