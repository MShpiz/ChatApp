package com.example.ChatSettingsService.storage.repositories;

import com.example.ChatSettingsService.storage.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long id);

    List<Chat> findAllByIsPrivate(boolean isPrivate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Chat c WHERE c.id = :userId")
    int deleteChatById(@Param("userId") Long userId);
}
