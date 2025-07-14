package com.example.MessageRecievingService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient provideClient() {
        return WebClient.builder()
                .baseUrl("http://chat-settings-service:8083/chats/chat")
                .build();
    }

}
