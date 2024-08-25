package com.project.SmartChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SmartChat.model.Message;
import com.project.SmartChat.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message findById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> findByGroupId(Long groupId) {
        return messageRepository.findByGroupId(groupId);
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
    
}
