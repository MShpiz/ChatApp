package com.example.HistoryService.controllers;


import com.example.HistoryService.services.AuthClient;
import com.example.HistoryService.services.ExceptionDescription;
import com.example.HistoryService.storage.ChatMessage;
import com.example.HistoryService.storage.MessageRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@AllArgsConstructor
public class HistoryController {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private EntityManager manager;

    @Autowired
    AuthClient client;


    @GetMapping("/history/")
    public Page<ChatMessage> getChatHistory(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long id,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) throws IllegalAccessException {
        if (!client.isUserAuthorised(authHeader).isValid()) throw new IllegalAccessException("invalid token");
        return repository.findAllByChatId(id, PageRequest.of(offset, limit));
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
}
