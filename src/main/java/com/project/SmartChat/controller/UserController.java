package com.project.SmartChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.project.SmartChat.model.User;
import com.project.SmartChat.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        //get logged in UserName through Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // get user details for the logged in userName
        String loggedInUserName = authentication.getName();
        
        // Find By UserName
        User loggedInUser = userService.findByUsername(loggedInUserName);
        return ResponseEntity.ok(loggedInUser);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User logged in successfully");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }
}
