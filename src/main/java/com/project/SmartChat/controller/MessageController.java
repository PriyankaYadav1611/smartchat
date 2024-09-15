package com.project.SmartChat.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Message;
import com.project.SmartChat.model.dto.MessageDTO;
import com.project.SmartChat.service.GroupService;
import com.project.SmartChat.service.MessageService;


@RestController
@RequestMapping("/api/groups/{groupId}/messages")
public class MessageController {
    
    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageService messageService;


    // Get all messages by groupID
    @GetMapping("")
    public ResponseEntity<List<MessageDTO>> getGroupById(@PathVariable("groupId") Long groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }

        // Make a list of all Messages related to this group
        List<Message> messages = messageService.getByGroupId(groupId);

        List<MessageDTO> messageDTOs = messages.stream().map(message -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setGroupId(message.getGroup().getId());
            messageDTO.setSenderId(message.getSender().getId());
            messageDTO.setContent(message.getContent());
            messageDTO.setIsSeen(message.getIsSeen());
            messageDTO.setSentAt(message.getSentAt());
            return messageDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOs);
    }

}
