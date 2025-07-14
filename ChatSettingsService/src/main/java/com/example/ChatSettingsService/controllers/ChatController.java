package com.example.ChatSettingsService.controllers;


import com.example.ChatSettingsService.commons.ExceptionDescription;
import com.example.ChatSettingsService.dto.ChatList;
import com.example.ChatSettingsService.dto.ChatUsers;
import com.example.ChatSettingsService.services.AuthClient;
import com.example.ChatSettingsService.storage.ChatStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/chats")
@AllArgsConstructor
public class ChatController {
    @Autowired
    ChatStorage storage;

    @Autowired
    AuthClient client;

    @GetMapping
    public ChatList getAllPublicChats(@RequestHeader("Authorization") String authHeader) throws IllegalAccessException {
        if (!client.isUserAuthorised(authHeader).isValid()) throw new IllegalAccessException("invalid token");
        return new ChatList(storage.getAllPublicChats());
    }

    @GetMapping("/user/{userId}")
    public ChatList getAllUserChats(@RequestHeader("Authorization") String authHeader, @PathVariable Long userId) throws IllegalAccessException {
        if (!client.isUserAuthorised(authHeader).isValid()) throw new IllegalAccessException("invalid token");
        return new ChatList(storage.getUserChats(userId));
    }

    @GetMapping("/chat/{chatId}")
    public ChatUsers getAllUsersInChat(@RequestHeader("Authorization") String authHeader, @PathVariable Long chatId) throws IllegalAccessException {
        if (!client.isUserAuthorised(authHeader).isValid()) throw new IllegalAccessException("invalid token");
        return new ChatUsers(storage.getChatUsers(chatId));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ExceptionDescription> handleIllegalArgument(IllegalAccessException exception) {
        return new ResponseEntity<>(
                new ExceptionDescription(
                        exception.getMessage(),
                        "403",
                        IllegalAccessException.class.getName(),
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(String::valueOf)
                                .toList()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDescription> handleIllegalArgument(NoSuchElementException exception) {
        return new ResponseEntity<>(
                new ExceptionDescription(
                        exception.getMessage(),
                        "404",
                        NoSuchElementException.class.getName(),
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(String::valueOf)
                                .toList()),
                HttpStatus.NOT_FOUND);
    }
}
