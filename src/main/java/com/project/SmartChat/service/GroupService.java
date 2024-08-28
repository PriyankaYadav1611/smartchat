package com.project.SmartChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.repository.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group createGroup(Group group) {
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