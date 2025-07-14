package com.example.MessageRecievingService.services;

import com.example.MessageRecievingService.dto.UserList;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ChatSettingsClient {
    @Autowired
    WebClient client;

    public boolean isUserInChat(Long chatId, Long userId) {
        UserList userList;
        try {
            userList = client.get()
                    .uri("/"+chatId.toString())
                    .retrieve()
                    .bodyToMono(UserList.class)
                    .block();
        } catch (RuntimeException e) {
            return false;
        }
        if (userList == null || userList.getUsers() == null) {
            return false;
        }
        return userList.getUsers().contains(userId);
    }
}
