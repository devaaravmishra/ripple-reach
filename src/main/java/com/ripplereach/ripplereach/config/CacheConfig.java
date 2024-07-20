package com.ripplereach.ripplereach.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CaffeineCacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("signedUrls");
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(7, TimeUnit.DAYS).maximumSize(1000));
    return cacheManager;
  }
}
