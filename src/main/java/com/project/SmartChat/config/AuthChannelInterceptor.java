package com.project.SmartChat.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside AuthChannelInterceptor : preSend");
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        String authToken = accessor.getFirstNativeHeader("Authorization");
        System.out.println("authToken: " + authToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("authentication: " + authentication);
        System.out.println("message: " + message);
        return message;
    }
}
