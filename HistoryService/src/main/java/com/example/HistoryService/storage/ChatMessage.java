package com.example.HistoryService.storage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Column(name = "text")
    private String text;

    @Column(name = "sentat")
    private LocalDateTime sentAt;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "chatid")
    private Long chatId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
}
