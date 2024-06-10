package com.ripplereach.ripplereach;

import com.ripplereach.ripplereach.config.OpenAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@Import(OpenAPIConfiguration.class)
public class RippleReachApplication {
  public static void main(String[] args) {
    SpringApplication.run(RippleReachApplication.class, args);
  }
}