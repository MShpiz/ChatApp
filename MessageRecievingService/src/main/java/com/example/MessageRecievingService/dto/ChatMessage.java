package com.example.MessageRecievingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String text;

    private LocalDateTime sentAt;

    private Long userId;

    private Long chatId;
}
