package com.example.MessageSendingService.services;

import com.example.MessageSendingService.dto.UserList;
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

    public List<Long> getUsersInChat(Long chatId) {
        UserList userList = client.get()
                .uri("/"+chatId.toString())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, error -> error.bodyToMono(String.class)
                        .map(NoSuchElementException::new))
                .bodyToMono(UserList.class)
                .block();
        if (userList == null || userList.getUsers() == null) {
            throw new IllegalArgumentException("chat settings error");
        }
        return userList.getUsers();
    }
}
