package com.example.MessageRecievingService.handlers;

import com.example.MessageRecievingService.config.kafkaConfig.KafkaMessageProducer;
import com.example.MessageRecievingService.dto.ChatMessage;
import com.example.MessageRecievingService.services.AuthClient;
import com.example.MessageRecievingService.services.ChatSettingsClient;
import com.example.MessageRecievingService.services.MessageParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class IncomingMessagesHandler extends TextWebSocketHandler {

    @Autowired
    MessageParser parser;

    @Autowired
    ChatSettingsClient client;

    @Autowired
    KafkaMessageProducer producer;

    @Autowired
    AuthClient authClient;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.atError().log(session.getHandshakeHeaders().getFirst("Authorization"));
        if (
                !authClient.isUserAuthorised(
                        session.getHandshakeHeaders().getFirst("Authorization")
                ).isValid()
        ) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        ChatMessage chatMessage = null;
        try {
            chatMessage = parser.parse(message.getPayload());
            if (client.isUserInChat(chatMessage.getChatId(), chatMessage.getUserId())) {
                producer.sendMessage("messages", message.getPayload());
                session.sendMessage(new TextMessage("message was sent"));
            } else {
                session.sendMessage(new TextMessage("message was not sent, you are not in chat"));
            }
        } catch (JsonProcessingException e) {
            log.atError().log("failed to convert message " + message);
            log.atError().log(e.getMessage());
        } catch (IOException e) {
            log.atError().log("failed o respond to error wia websocket" + message);
            log.atError().log(e.getMessage());
        }

    }

}