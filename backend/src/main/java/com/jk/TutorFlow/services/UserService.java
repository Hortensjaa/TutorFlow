package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo ;

    public void addUser(User user) {
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
