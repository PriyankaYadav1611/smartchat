package com.project.SmartChat.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.JwtResponse;
import com.project.SmartChat.model.User;
import com.project.SmartChat.model.dto.GroupDTO;
import com.project.SmartChat.service.JwtService;
import com.project.SmartChat.service.UserService;
import com.project.SmartChat.service.ParticipantService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ParticipantService participantService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        // check if logged in user can only update itself, it shouldn't be allowed to update others
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get LoggedIn user's name
        String loggedInUserName = authentication.getName();

        // check if loggedInUserName is same as id's userName
        User loggedInUser = userService.findByUsername(loggedInUserName);
        if (!loggedInUser.getId().equals(id)) {
            // Logged in user is requestig update for other user id
            return ResponseEntity.status(403).build();
        }
        
        // Logged in user is requesting update for self
        Optional<User> updatedUser = userService.updateUser(id, user);
        return updatedUser.map((u) -> ResponseEntity.ok(u))
        .orElseGet(() -> ResponseEntity.notFound().build());
        // if (updatedUser.isEmpty()) {
        //     return  ResponseEntity.notFound().build();
        // }
        // return ResponseEntity.ok(updatedUser.get());
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(){
        // Get logged in UserName through Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user details for the logged in userName
        String loggedInUserName = authentication.getName();
        System.out.println("getMe: auth.getName: " + loggedInUserName);
        
        // Find By UserName
        User loggedInUser = userService.findByUsername(loggedInUserName);
        System.out.println("LoggedIn User: " + loggedInUser);
        return ResponseEntity.ok(loggedInUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<User> removeUser() {
        // Get authentication principle
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get Looged in userName from authentication object
        String loggedInUserName = authentication.getName();

        // Delete loggedInUser from DB
        Optional<User> deletedUser = userService.deleteByUserName(loggedInUserName);
        User delUser = deletedUser.orElseGet(()-> null);
        if (delUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(delUser);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));        
        // return ResponseEntity.ok("User logged in successfully");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }

    @GetMapping("/groups")
    public List<GroupDTO> getAllGroupsWithLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get Looged in userName from authentication object
        String loggedInUserName = authentication.getName();

        User loggedInUser = userService.findByUsername(loggedInUserName);
        
        // By loggedin userId, participant service will return list of participant's object
        List<Group> groups = participantService.getAllGroupsWithUserId(loggedInUser.getId());

        // Now, create GroupDTO to return
        List<GroupDTO> groupDTOs = groups.stream().map(group -> {
            // Need groupmember and groupmember is property in groupDTO, so, call participant service 
            List<User> users =  participantService.getUsersByGroupId(group.getId());

            // Covert Participant list type to Long 
            List<Long> membersOfGroup = users.stream().map(user -> {
                return user.getId();
            }).collect(Collectors.toList());

            GroupDTO groupDto = new GroupDTO();
            groupDto.setId(group.getId());
            groupDto.setTitle(group.getTitle());
            groupDto.setDescription(group.getDescription());
            groupDto.setCreatedById(group.getCreatedBy().getId());
            groupDto.setCreatedDate(group.getcreatedDate());
            groupDto.setType(group.getType());

            // pass list of membersofGroup
            groupDto.setGroupMembers(membersOfGroup);
            return groupDto;
        }).collect(Collectors.toList());

        return groupDTOs;
    }
}

