package com.project.SmartChat.handlers;

import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession; 
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus; 
import org.springframework.web.socket.WebSocketMessage; 

import java.util.ArrayList; 
import java.util.Collections; 
import java.util.List;


public class SocketConnectionHandler extends TextWebSocketHandler {
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("User with sessionId: " + session.getId() + " Connected");
        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("User with sessionId: " + session.getId() + " Disconnected");
        webSocketSessions.remove(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        
        super.handleMessage(session, message);
        
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (session == webSocketSession)
                continue;

            webSocketSession.sendMessage(message);
        }
    } 
}
