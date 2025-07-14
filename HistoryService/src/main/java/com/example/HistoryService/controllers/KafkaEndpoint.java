package com.example.HistoryService.controllers;

import com.example.HistoryService.storage.MessageParser;
import com.example.HistoryService.storage.ChatMessage;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class KafkaEndpoint {

    @Autowired
    EntityManager manager;

    @Autowired
    MessageParser parser;

    @KafkaListener(topics = {"#{'${spring.kafka.template.default-topic}'}"}, groupId = "group2")
    @SneakyThrows
    @Transactional
    public void getUpdates(String message) {
        ChatMessage chatMessage = parser.parse(message);
        manager.persist(chatMessage);
    }
}
