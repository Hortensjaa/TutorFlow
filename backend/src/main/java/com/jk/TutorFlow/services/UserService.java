package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User updateUser(UserModel model) {
        User user = userRepository.findById(model.getID()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(model.getUsername());
        return userRepository.save(user);
    }

    public UserModel generateModel(User entity) {
        UserModel model = new UserModel();
        model.setID(entity.getUser_id());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        return model;
    }
}
