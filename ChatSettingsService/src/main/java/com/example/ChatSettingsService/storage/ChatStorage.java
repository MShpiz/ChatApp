package com.example.ChatSettingsService.storage;

import com.example.ChatSettingsService.dto.NewChatInfo;
import com.example.ChatSettingsService.storage.entities.Chat;
import com.example.ChatSettingsService.storage.entities.UserChat;
import com.example.ChatSettingsService.storage.repositories.ChatRepository;
import com.example.ChatSettingsService.storage.repositories.UserChatRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ChatStorage {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    EntityManager manager;

    @Autowired
    UserChatRepository userChatRepository;

    public List<Chat> getAllPublicChats() {
        return chatRepository.findAllByIsPrivate(false);
    }

    public List<Chat> getUserChats(Long userId) {
        return userChatRepository
                .findAllByUserId(userId)
                .stream()
                .map(UserChat::getChat)
                .toList();
    }

    @Transactional
    public Chat createChat(NewChatInfo info, long userId) throws IllegalAccessException {
        Chat chat = new Chat();
        chat.setName(info.name());
        chat.setDescription(info.description());
        chat.setOwner(userId);
        chat.setPrivate(info.isPrivate());

        manager.persist(chat);

        addUser(chat.getId(), userId, userId);

        return chat;
    }

    @Transactional
    public Chat updateChat(long chatId, NewChatInfo info, long userId) throws IllegalAccessException {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        if (chat.getOwner() != userId) throw new IllegalAccessException("you are not the owner of this chat");
        chat.setName(info.name());
        chat.setDescription(info.description());
        chat.setPrivate(info.isPrivate());

        manager.persist(chat);

        return chat;
    }

    public void delete(long chatId, long userId) throws IllegalAccessException {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        if (chat.getOwner() != userId) throw new IllegalAccessException("you are not the owner of this chat");
        chatRepository.deleteChatById(chatId);
    }

    public List<Long> getChatUsers(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return userChatRepository
                .findAllByChat(chat)
                .stream()
                .map(userChat -> userChat.getChat().getId())
                .toList();
    }

    @Transactional
    public void addUser(Long chatId, Long userId, Long owner) throws IllegalAccessException {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        if (chat.getOwner() != owner) throw new IllegalAccessException("you are not the owner of this chat");
        UserChat userChat = new UserChat();
        userChat.setChat(chat);
        userChat.setUserId(userId);

        manager.persist(userChat);
    }
}
