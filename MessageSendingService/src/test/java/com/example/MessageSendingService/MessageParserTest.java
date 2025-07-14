package com.example.MessageSendingService;


import com.example.MessageSendingService.dto.ChatMessage;
import com.example.MessageSendingService.services.MessageParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageParserTest {

    private MessageParser parser;

    @BeforeEach
    public void before() {
        parser = new MessageParser();
    }

    @Test
    public void correctMessageTest() throws JsonProcessingException {
        String message = """
                    {
                    "text": "aaa",
                    "sentAt": "2025-07-11T19:30:05.375989",
                    "userId": 1,
                    "chatId": 1
                }""";
        ChatMessage correct = new ChatMessage("aaa", LocalDateTime.parse("2025-07-11T19:30:05.375989"), 1L, 1L);

        ChatMessage result = parser.parse(message);

        assertThat(result).isEqualTo(correct);

    }

    @Test
    public void incorrectMessageTest() {
        String message = """
                {
                    "aaa": "text"
                }
                """;
        JsonProcessingException exception = null;

        try {
            parser.parse(message);
        } catch (JsonProcessingException e) {
            exception = e;
        }

        assertThat(exception).isNotNull();

    }
}
