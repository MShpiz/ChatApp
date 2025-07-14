package com.example.MessageRecievingService.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ChatMessage {
    private String text;

    private LocalDateTime sentAt;

    private Long userId;

    private Long chatId;
}
