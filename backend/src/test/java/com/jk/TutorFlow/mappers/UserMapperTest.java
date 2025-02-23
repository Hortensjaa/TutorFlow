package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testToModel() {
        // Create a User entity for testing
        User user = new User();
        user.setUser_id(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        // Convert the User entity to a UserModel
        UserModel userModel = userMapper.toModel(user);

        // Assert that the UserModel is correctly mapped
        assertNotNull(userModel);
        assertEquals(1L, userModel.getID()); // ID should match
        assertEquals("john_doe", userModel.getUsername()); // Username should match
        assertEquals("john.doe@example.com", userModel.getEmail()); // Email should match
    }

    @Test
    void testToEntity() {
        // Create a UserModel for testing
        UserModel userModel = new UserModel();
        userModel.setID(2L);
        userModel.setUsername("jane_doe");
        userModel.setEmail("jane.doe@example.com");

        // Convert the UserModel to a User entity
        User user = userMapper.toEntity(userModel);

        // Assert that the User entity is correctly mapped
        assertNotNull(user);
        assertEquals(2L, user.getUser_id()); // ID should match
        assertEquals("jane_doe", user.getUsername()); // Username should match
        assertEquals("jane.doe@example.com", user.getEmail()); // Email should match
    }
}
