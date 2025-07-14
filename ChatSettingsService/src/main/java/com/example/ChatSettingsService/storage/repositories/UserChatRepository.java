package com.example.ChatSettingsService.storage.repositories;

import com.example.ChatSettingsService.storage.entities.Chat;
import com.example.ChatSettingsService.storage.entities.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    List<UserChat> findAllByUserId(Long userId);
    List<UserChat> findAllByChat(Chat chat);
}
