package com.project.SmartChat.repository;

import java.util.List;

import com.project.SmartChat.model.Group;


public interface GroupRepository {
    
    public Group save(Group group);

    public Group findById(Long id);

    public List<Group> findAll();

    public void delete(Group group);
}
