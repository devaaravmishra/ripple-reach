package com.ripplereach.ripplereach.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;


@Configuration
public class FileUploadConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        multipartConfigFactory.setMaxFileSize(DataSize.ofMegabytes(10)); // set the max file size
        multipartConfigFactory.setMaxRequestSize(DataSize.ofMegabytes(10)); // set the max request size
        return multipartConfigFactory.createMultipartConfig();
    }
}
