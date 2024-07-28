package com.project.SmartChat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry; 
import com.project.SmartChat.handlers.SocketConnectionHandler; 
import org.springframework.web.socket.config.annotation.WebSocketConfigurer; 


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new SocketConnectionHandler(), "/letschat").setAllowedOrigins("*");
    }
}
