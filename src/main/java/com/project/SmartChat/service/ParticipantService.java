package com.project.SmartChat.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.ParticipantId;
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

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;


    public List<Participant> addParticipants(Long groupId, List<Long> userIds) {
    
        // Get All groups
        Group group = groupService.getGroupById(groupId);
    
        // create User list
        List<User> users = userIds.stream().map(userId -> {
            Optional<User> optionalUser = userService.findByUserId(userId);

            return optionalUser.map((u) -> u).orElseGet(() -> null);
            // TODO: null return case to be handled properly by calling function or this function.

        }).collect(Collectors.toList());


        List<Participant> participants = users.stream().map(user -> {
            Participant participant = new Participant(group, user);
            return participant;

        }).collect(Collectors.toList());

        participantRepository.saveAll(participants);
        return participants;
    }
 
    public List<User> getUsersByGroupId(Long groupId) {
        return participantRepository.getUsersByGroupId(groupId);
    }

    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds) {
        return participantRepository.findGroupsWithExactParticipants(participantUserIds);
    }

    public boolean isUserIdParticipantOfGroupId(Long userId, Long groupId) {
        return participantRepository.isUserIdParticipantOfGroupId(userId, groupId);
    }

    public void deleteParticipantById(ParticipantId participantId) {
        Participant participant = participantRepository.findById(participantId);
        if (participant != null) {
            participantRepository.delete(participant);
        }
    }

    public List<Group> getAllGroupsWithUserId(Long userId) {
        return participantRepository.getAllGroupsWithUserId(userId);
    }

}