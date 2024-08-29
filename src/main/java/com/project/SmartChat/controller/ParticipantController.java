package com.project.SmartChat.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.SmartChat.service.ParticipantService;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.ParticipantId;
import com.project.SmartChat.model.dto.ParticipantDTO;


@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    // Initially when user has not send msg to perticular group then it will add participant in participant table 
    @PostMapping("")
    public ResponseEntity<ParticipantDTO> addParticipant(@RequestBody ParticipantDTO participantDTO) {
        // Find the User by ID
        Long userId = participantDTO.getUserId();
        Long groupId = participantDTO.getGroupId();

        System.out.println("Participant Controller ................");
        Participant savedParticipant = participantService.addParticipant(groupId, userId);

        return ResponseEntity.ok(new ParticipantDTO(savedParticipant.getGroup().getId(), savedParticipant.getUser().getId()));
    }


    @GetMapping("/groups/{id}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantByGroupId(@PathVariable("id") Long groupId) {
        List<Participant> participants = participantService.getParticipantsByGroupId(groupId);
        
        List<ParticipantDTO> participantDTOs = participants.stream().map(participant -> {
            ParticipantDTO dto = new ParticipantDTO();
            dto.setGroupId(participant.getGroup().getId());
            dto.setUserId(participant.getUser().getId());
            
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(participantDTOs);
    }


    @DeleteMapping("")
    public ResponseEntity<ParticipantDTO> deleteParticipantFromGroup(@RequestBody ParticipantId participantId) {
        
        Long userId = participantId.getUserId();
        Long groupId = participantId.getGroupId();

        boolean isUserExistInGroup = participantService.isUserIdParticipantOfGroupId(userId, groupId);

        if (isUserExistInGroup) {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setGroupId(participantId.getGroupId());
            participantDTO.setUserId(participantId.getUserId());

            participantService.deleteParticipantById(participantId);

            return ResponseEntity.ok(participantDTO); 
        }
        
        return ResponseEntity.status(403).build(); 
    }
    
}
