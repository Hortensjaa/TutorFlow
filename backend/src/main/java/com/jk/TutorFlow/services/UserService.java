package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.UserMapper;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserModel getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return userMapper.toModel(user);
    }

    public UserModel updateUser(UserModel model) {
        User user = userRepository.findById(model.getID()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(model.getUsername());
        userRepository.save(user);
        return userMapper.toModel(user);
    }
}
