package com.project.SmartChat.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.User;
import com.project.SmartChat.model.dto.GroupDTO;
import com.project.SmartChat.model.dto.UserDTO;
import com.project.SmartChat.service.GroupService;
import com.project.SmartChat.service.ParticipantService;
import com.project.SmartChat.service.UserService;


@RestController
@RequestMapping("/api/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipantService participantService;

    @PostMapping("")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        // Get loggedIn username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserName = authentication.getName();

        // get loggedIn User
        User user = userService.findByUsername(loggedInUserName);
        
        Group group = new Group();
        group.setTitle(groupDTO.getTitle());
        group.setDescription(groupDTO.getDescription());
        group.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
        group.setCreatedBy(user);
        group.setType(groupDTO.getType());
    
        // Save the Group
        Group savedGroup = groupService.createGroup(group);
        Long savedGroupId = savedGroup.getId();

        List<Long> userIds = groupDTO.getGroupMembers();

        // Adding participants and group mapping in participant table
        List<Participant> participants = participantService.addParticipants(savedGroupId, userIds);

        GroupDTO savedGroupDTO = new GroupDTO();
        savedGroupDTO.setId(savedGroupId);
        savedGroupDTO.setTitle(savedGroup.getTitle());
        savedGroupDTO.setDescription(savedGroup.getDescription());
        savedGroupDTO.setType(savedGroup.getType());
        savedGroupDTO.setCreatedById(savedGroup.getCreatedBy().getId());
        savedGroupDTO.setGroupMembers(userIds);
        // Return the created Group
        return ResponseEntity.ok(savedGroupDTO);
    }

    // Get a Group by ID
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") Long groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group != null) {
            GroupDTO groupDto  = new GroupDTO();
            groupDto.setId(group.getId());
            groupDto.setCreatedDate(group.getcreatedDate());
            groupDto.setDescription(group.getDescription());
            groupDto.setTitle(group.getTitle());
            groupDto.setType(group.getType());
            groupDto.setCreatedById(group.getCreatedBy().getId());

            return ResponseEntity.ok(groupDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all Groups
    @GetMapping("")
    public ResponseEntity<List<GroupDTO>> getAllGroups(){
        List<Group> groups = groupService.getAllGroups();
        
        List<GroupDTO> groupDTO = groups.stream().map(group -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(group.getId());
            dto.setTitle(group.getTitle());
            dto.setDescription(group.getDescription());
            dto.setCreatedDate(group.getcreatedDate());
            dto.setCreatedById(group.getCreatedBy().getId());
            dto.setType(group.getType());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(groupDTO);
    }

    // Delete a group by groupid..
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteGroupById(@PathVariable("id") Long groupId) {

        Group group = groupService.getGroupById(groupId);
        if(group != null) {
            groupService.deleteGroupById(groupId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // Edit existing Group by id 
    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable("id") Long groupId, @RequestBody GroupDTO groupDTO) {
        
        Group group = groupService.getGroupById(groupId);

        if (group != null) {
            String title = groupDTO.getTitle();
            String description = groupDTO.getDescription();
            
            if(title != null) {
                group.setTitle(title);
            }
            if(description != null) {
                group.setDescription(description);
            }

            Group updatedGroup = groupService.createGroup(group);

            GroupDTO groupDto = new GroupDTO();
            groupDto.setId(updatedGroup.getId());
            groupDto.setCreatedDate(updatedGroup.getcreatedDate());
            groupDto.setDescription(updatedGroup.getDescription());
            groupDto.setTitle(updatedGroup.getTitle());
            groupDto.setType(updatedGroup.getType());
            groupDto.setCreatedById(updatedGroup.getCreatedBy().getId());

            return ResponseEntity.ok(groupDto);
        }
        else {
            return ResponseEntity.notFound().build();
        }
       
    }

    // Get all unique users where logged in user is a member
    @GetMapping("/users")
    public List<UserDTO> getAllUsersForLoggedInUserGroups() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get Looged in userName from authentication object
        String loggedInUserName = authentication.getName();

        User loggedInUser = userService.findByUsername(loggedInUserName);
        
        // By loggedin userId, participant service will return list of participant's object
        List<Group> groups = participantService.getAllGroupsWithUserId(loggedInUser.getId());

        List<Long> groupIds = groups.stream().map(group -> {
            Long groupId = group.getId();
            return groupId;
        }).collect(Collectors.toList());

        List<User> users = participantService.getUsersByGroupIds(groupIds);

        List<UserDTO> userDTOs = users.stream().map(user -> {
            Long userId = user.getId();
            String username = user.getUsername();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setUsername(username);
            return userDTO;
        }).collect(Collectors.toList());
        
        return userDTOs;
    }

}
