package com.example.MessageSendingService.handlers;

import com.example.MessageSendingService.dto.ChatMessage;
import com.example.MessageSendingService.services.AuthClient;
import com.example.MessageSendingService.services.ChatSettingsClient;
import com.example.MessageSendingService.services.MessageParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OutgoingMessagesHandler implements WebSocketHandler {

    @Autowired
    ChatSettingsClient client;

    @Autowired
    MessageParser parser;

    @Autowired
    AuthClient authClient;

    private final Map<WebSocketSession, Long> userSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (
                !authClient.isUserAuthorised(
                        session.getHandshakeHeaders().getFirst("Authorization")
                ).isValid()
        ) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        userSessions.put(session, 1L);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        userSessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @KafkaListener(topics = {"#{'${spring.kafka.template.default-topic}'}"}, groupId = "group1")
    @SneakyThrows
    public void getMessages(String message) {

        ChatMessage chatMessage = parser.parse(message);

        List<Long> users = client.getUsersInChat(chatMessage.getChatId());

        userSessions
                .entrySet()
                .stream()
                .filter(userSession -> users.contains(userSession.getValue()))
                .forEach(userSession -> sendMessage(userSession.getKey(), message));
    }

    private void sendMessage(WebSocketSession session, String message) {
        WebSocketMessage<?> wbMessage = new TextMessage(message);

        try {
            session.sendMessage(wbMessage);
        } catch (IOException e) {
            log.atError().log(e.getMessage());
        }
    }

}