package com.example.HistoryService.storage;

import com.example.HistoryService.storage.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findAllByChatId(Long chatId, Pageable pageable);
}
