package com.example.ChatSettingsService.dto;

import com.example.ChatSettingsService.storage.entities.Chat;

import java.util.List;

public record ChatList(List<Chat>chats) {
}
