package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

/**
 * @author dizhang
 * @date 2021-03-16
 */
@Component
public class WebSocketEventListener {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handlerWebSocketConnectListener(SessionConnectEvent event) {
        logger.info("Received a new web socket connection {} ", event.getMessage());
    }

    @EventListener
    public void handlerWebSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if (username != null) {
            logger.info("user {}  disconnect...", event.getMessage());
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageType(ChatMessage.MessageType.LEAVE);
            chatMessage.setContent("user leave...");
            chatMessage.setSender(username);
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }

    }
}
