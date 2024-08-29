package com.project.SmartChat.repository;

import java.util.List;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.ParticipantId;

public interface  ParticipantRepository {
    
    public Participant save(Participant participant);

    public List<Participant> findByGroupId(Long groupId);

    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds);

    public Participant findById(ParticipantId id);

    public boolean isUserIdParticipantOfGroupId(Long userId, Long groupId);

    public void delete(Participant participant);
}
