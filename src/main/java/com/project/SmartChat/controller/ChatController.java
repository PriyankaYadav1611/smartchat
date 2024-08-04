package com.project.SmartChat.controller;

import com.project.SmartChat.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@RequestBody ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, Authentication authentication) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside ChatController.sendMessage");
        System.out.println("----------------------------------------------------------------------");
        String recipient = chatMessage.getRecipient();
        System.out.println("Recipient " + recipient);
        System.out.println("Content " + chatMessage.getContent());

        messagingTemplate.convertAndSendToUser(recipient, "/queue/messages", chatMessage);
    }
}
