package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserModel toModel(User entity) {
        UserModel model = new UserModel();
        model.setID(entity.getUser_id());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        return model;
    }

    public User toEntity(UserModel model) {
        User user = new User();
        user.setUser_id(model.getID());
        user.setEmail(model.getEmail());
        user.setUsername(model.getUsername());
        return user;
    }
}
