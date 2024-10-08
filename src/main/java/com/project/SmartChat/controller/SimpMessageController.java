package com.project.SmartChat.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Message;
import com.project.SmartChat.model.User;
import com.project.SmartChat.model.dto.GroupDTO;
import com.project.SmartChat.model.dto.MessageDTO;
import com.project.SmartChat.service.GroupService;
import com.project.SmartChat.service.MessageService;
import com.project.SmartChat.service.ParticipantService;
import com.project.SmartChat.service.UserService;


@Controller
public class SimpMessageController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    public SimpMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        
    }


    // send message support
    // Limitation: As of now, one to one message is only supported
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@RequestBody MessageDTO messageDto, SimpMessageHeaderAccessor headerAccessor, Authentication authentication) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside ChatController.sendMessage");
        System.out.println("----------------------------------------------------------------------");
        // TODO: check if authentication is set
       
        Long senderId = messageDto.getSenderId();
        Long sendToGroupId = messageDto.getGroupId();
        String content = messageDto.getContent();

        // get sender
        User sender = userService.findByUserId(senderId).get();

        if (sender == null) {
            System.out.println("Provided sender does not exist in database");
            return;
        }

        // Check if this groupUser is member of the group 'sendToGroup'
        if (!participantService.isUserIdParticipantOfGroupId(senderId, sendToGroupId)) {
            System.out.println("sender is not a member of the group");
            return;
        }
        // sender 'sender' is part of the group 'sendToGroup'

        Group sendToGroup = groupService.getGroupById(sendToGroupId);
        if (sendToGroup == null) {
            System.out.println("Group does not exist...............");
            return;
        }

        System.out.println("Content " + messageDto.getContent());
        List<User> groupUsers =  participantService.getUsersByGroupId(sendToGroupId);

        Message message = new Message();
        message.setSender(sender);
        message.setGroup(sendToGroup); 
        message.setContent(content);
        message.setSentAt(new Date());
        
        message = messageService.saveMessage(message);
        messageDto.setSentAt(message.getSentAt());
        messageDto.setId(message.getId());
        
        String destinatioString ="/queue/messages/groups";
        System.out.println("destinatioString: " + destinatioString);
        messagingTemplate.convertAndSendToUser("groupId-" + sendToGroupId, destinatioString, messageDto);
    }
}
