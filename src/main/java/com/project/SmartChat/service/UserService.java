package com.project.SmartChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SmartChat.model.User;
import com.project.SmartChat.repository.UserCustomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserCustomRepository userCustomRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userCustomRepository.save(user);
    }

    public User findByUsername(String username) {
        return userCustomRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userCustomRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }


    public List<User> getAllUsers(){
        List<User> users = userCustomRepository.getAllUsers();
        return users;
    }

    public Optional<User> updateUser(Long id, User user) {
        Optional<User> u = userCustomRepository.findById(id);
        return u.map((foundUser) -> {
            foundUser.setUsername(user.getUsername());
            return userCustomRepository.save(foundUser);
        });
    }

    public Optional<User> deleteByUserName(String username) {
        User user = userCustomRepository.findByUsername(username);
        if(user == null){
            return Optional.empty();
        }

        int deletedRowsCount = userCustomRepository.deleteByUserName(user.getUsername());
        if(deletedRowsCount>0){
            return Optional.ofNullable(user);
        }
        else{
            return Optional.empty();
        }
    }

    public Optional<User> findByUserId(Long id) {
        return userCustomRepository.findById(id);
    } 
}