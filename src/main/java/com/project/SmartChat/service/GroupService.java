package com.project.SmartChat.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SmartChat.enums.GroupType;
import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.User;
import com.project.SmartChat.repository.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group createGroup(String title, String description, Date createdDate, User createdBy, GroupType type) {
        Group group = new Group(title, description, createdDate, createdBy, type);
        return groupRepository.save(group);
    }

    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public void deleteGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId);
        if (group != null) {
            groupRepository.delete(group);
        }
    }
}