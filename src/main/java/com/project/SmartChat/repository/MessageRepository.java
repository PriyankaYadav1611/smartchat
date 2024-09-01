package com.project.SmartChat.repository;

import java.util.List;

import com.project.SmartChat.model.Message;

public interface  MessageRepository {

    public Message save(Message message);

    public Message findById(Long id);

    List<Message> getByGroupId(Long groupId);

    public void delete(Message message);
}
