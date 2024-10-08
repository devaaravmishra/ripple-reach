package com.ripplereach.ripplereach;

import com.ripplereach.ripplereach.config.OpenAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableSpringDataWebSupport(
    pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableAsync
@EnableCaching
@Import(OpenAPIConfiguration.class)
public class RippleReachApplication {
  public static void main(String[] args) {
    SpringApplication.run(RippleReachApplication.class, args);
  }

  //  @Bean
  //  public CommandLineRunner run(MigrationService migrationService) {
  //    return args -> {
  //      migrationService.migrateImages("src/main/resources/static/images/", "images/");
  //    };
  //  }
}
