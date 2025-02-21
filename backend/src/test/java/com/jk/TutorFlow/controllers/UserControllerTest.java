package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.StudentService;
import com.jk.TutorFlow.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        StudentModel studentModel = new StudentModel();
        studentModel.setName("Student Name");
        when(studentService.generateModel(any())).thenReturn(studentModel);
    }

    @Test
    void testAddUser() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        when(userService.getUserByEmail("test@example.com")).thenReturn(null);

        RedirectView result = userController.addUser(principal);

        assertNotNull(result);
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User("Test User", "test@example.com");
        when(userService.getUserById(1L)).thenReturn(user);

        User result = userController.getUserById("1");

        assertNotNull(result);
        assertEquals("Test User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetActiveUser() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        User user = new User("Test User", "test@example.com");
        user.setUser_id(1L);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        UserModel result = userController.getActiveUser(principal).getBody();

        assertNotNull(result);
        assertEquals("Test User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setUsername("Updated User");

        User updatedUser = new User("Updated User", "test@example.com");
        when(userService.updateUser(userModel)).thenReturn(updatedUser);

        UserModel result = userController.updateUser(principal, userModel).getBody();

        assertNotNull(result);
        assertEquals("Updated User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }
}