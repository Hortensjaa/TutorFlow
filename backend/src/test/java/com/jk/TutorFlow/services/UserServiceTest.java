package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.UserMapper;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserModel userModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUser_id(1L);
        user.setUsername("john.doe@example.com");

        userModel = new UserModel();
        userModel.setID(1L);
        userModel.setUsername("john.doe@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toModel(any(User.class))).thenReturn(userModel);
    }

    @Test
    public void testAddUserSuccess() {
        userService.addUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetEntityByEmailSuccess() {
        User result = userService.getEntityByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUsername());
    }

    @Test
    public void testGetEntityByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User result = userService.getEntityByEmail("nonexistent@example.com");

        assertEquals(null, result);
    }

    @Test
    public void testGetUserByEmailSuccess() {
        UserModel result = userService.getUserByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUsername());
    }

    @Test
    public void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        UserModel result = userService.getUserByEmail("nonexistent@example.com");

        assertEquals(null, result);
    }

    @Test
    public void testUpdateUserSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserModel result = userService.updateUser(userModel);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.updateUser(userModel);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}
