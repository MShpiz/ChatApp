package com.example.ChatSettingsService.controllers;

import com.example.ChatSettingsService.commons.ExceptionDescription;
import com.example.ChatSettingsService.dto.NewChatInfo;
import com.example.ChatSettingsService.dto.NewUser;
import com.example.ChatSettingsService.services.AuthClient;
import com.example.ChatSettingsService.storage.ChatStorage;
import com.example.ChatSettingsService.storage.entities.Chat;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatSettingsController {

    @Autowired
    ChatStorage storage;

    @Autowired
    AuthClient client;

    @PostMapping()
    public Chat createChat(@RequestHeader("Authorization") String authHeader, @RequestBody NewChatInfo info) throws IllegalAccessException {
        var authResult = client.isUserAuthorised(authHeader);
        if (!authResult.isValid()) throw new IllegalAccessException("invalid token");

        return storage.createChat(info, authResult.userId());
    }

    @PutMapping("/{chatId}")
    public Chat updateChat(@RequestHeader("Authorization") String authHeader, @PathVariable long chatId, @RequestBody NewChatInfo info) throws IllegalAccessException {
        var authResult = client.isUserAuthorised(authHeader);
        if (!authResult.isValid()) throw new IllegalAccessException("invalid token");
        return storage.updateChat(chatId, info, authResult.userId());

    }

    @DeleteMapping("/{chatId}")
    public void deleteChat(@RequestHeader("Authorization") String authHeader, @PathVariable long chatId) throws IllegalAccessException {
        var authResult = client.isUserAuthorised(authHeader);
        if (!authResult.isValid()) throw new IllegalAccessException("invalid token");

        storage.delete(chatId, authResult.userId());
    }

    @PostMapping("/{chatId}/add")
    public void addUserToChat(@RequestHeader("Authorization") String authHeader, @PathVariable Long chatId, @RequestBody NewUser user) throws IllegalAccessException {
        var authResult = client.isUserAuthorised(authHeader);
        if (!authResult.isValid()) throw new IllegalAccessException("invalid token");

        storage.addUser(chatId, user.userId(), authResult.userId());
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
