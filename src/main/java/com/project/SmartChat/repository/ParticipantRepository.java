package com.project.SmartChat.repository;

import java.util.List;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.ParticipantId;
import com.project.SmartChat.model.User;

public interface  ParticipantRepository {
    
    public Participant save(Participant participant);

    public void saveAll(List<Participant> participants);

    public List<User> getUsersByGroupId(Long groupId);

    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds);

    public Participant findById(ParticipantId id);

    public boolean isUserIdParticipantOfGroupId(Long userId, Long groupId);

    public void delete(Participant participant);

    public List<Group> getAllGroupsWithUserId(Long userId);
}
