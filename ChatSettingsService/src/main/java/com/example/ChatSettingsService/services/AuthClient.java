package com.example.ChatSettingsService.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthClient {

    WebClient client = WebClient.builder()
            .baseUrl("http://auth-service:8084/getUserByToken")
            .build();

    public UserAuthResponse isUserAuthorised(String token) {
        UserAuthResponse response;
        try {
            response = client.post()
                    .bodyValue(new UserAuthRequest(token))
                    .retrieve()
                    .bodyToMono(UserAuthResponse.class)
                    .block();
        } catch (RuntimeException e) {
            return new UserAuthResponse(false, -1L);
        }

        return response;
    }
}
