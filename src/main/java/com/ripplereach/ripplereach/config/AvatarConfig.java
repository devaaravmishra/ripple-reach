package com.ripplereach.ripplereach.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableAsync
public class AvatarConfig {

    @Value("${static.resource.url}")
    private String staticResourceUrl;

    // The avatarList is initialized as a synchronized list to ensure thread-safety when accessing or modifying the list.
    @Getter
    private final static List<String> avatarList = Collections.synchronizedList(new ArrayList<>());
    private final ResourcePatternResolver resourcePatternResolver;

    public AvatarConfig(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @PostConstruct
    @Async
    // Async Initialization: The @Async annotation on the loadAvatarList method allows the avatars to be loaded asynchronously after the application starts, preventing startup delays.
    public void loadAvatarList() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:/static/avatars/*");
            for (Resource resource : resources) {
                avatarList.add("/avatars/" + resource.getFilename());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load avatar list", e);
        }
    }

    @Bean
    public List<String> avatarList(){
        return avatarList;
    }
}
