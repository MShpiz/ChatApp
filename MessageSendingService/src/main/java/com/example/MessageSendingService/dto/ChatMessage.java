package com.example.MessageSendingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class ChatMessage {
    private String text;

    private LocalDateTime sentAt;

    private Long userId;

    private Long chatId;
}
