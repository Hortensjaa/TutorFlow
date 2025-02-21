package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.addUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByIdNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmailNotFound() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateUser() {
        // Arrange
        UserModel model = new UserModel();
        model.setID(1L);
        model.setUsername("newUsername");

        User user = new User();
        when(userRepository.findById(model.getID())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.updateUser(model);

        // Assert
        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        verify(userRepository, times(1)).findById(model.getID());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        // Arrange
        UserModel model = new UserModel();
        model.setID(1L);
        when(userRepository.findById(model.getID())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(model));
        verify(userRepository, times(1)).findById(model.getID());
        verify(userRepository, never()).save(any());
    }
}