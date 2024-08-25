package com.project.SmartChat.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.User;
import com.project.SmartChat.repository.GroupRepository;
import com.project.SmartChat.repository.ParticipantRepository;
import com.project.SmartChat.repository.UserCustomRepository;

@Service
public class ParticipantService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserCustomRepository userCustomRepository;

    @Autowired
    private ParticipantRepository participantRepository;


    public Participant addParticipant(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new RuntimeException("Group not found");
        }
        try {
            User user = userCustomRepository.findById(userId).get();
            Participant participant = new Participant(group, user);
            return participantRepository.save(participant);
        } catch (NoSuchElementException e) {
            System.out.println("The user you want to add as participant doesn't exist\nexception.." + e);
        }

        return null;
    }

    public List<Participant> getParticipantsByGroupId(Long groupId) {
        return participantRepository.findByGroupId(groupId);
    }

    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds) {
        return participantRepository.findGroupsWithExactParticipants(participantUserIds);
    }

    public void deleteParticipantById(Long participantId) {
        Participant participant = participantRepository.findById(participantId);
        if (participant != null) {
            participantRepository.delete(participant);
        }
    }
}