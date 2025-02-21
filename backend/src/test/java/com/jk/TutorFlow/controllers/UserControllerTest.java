package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
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

    @InjectMocks
    private UserController userController;
    @Mock
    private com.jk.TutorFlow.utils.PrincipalExtractor PrincipalExtractor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(PrincipalExtractor.getUserFromPrincipal(any())).thenReturn(new User("Test User", "test@example.com"));
        when(PrincipalExtractor.createUserFromPrincipal(any())).thenReturn(new User("Test User", "test@example.com"));
    }

    @Test
    void testAddUser() {
        OAuth2User principal = mock(OAuth2User.class);
        when(userService.getUserByEmail("test@example.com")).thenReturn(null);

        RedirectView result = userController.addUser(principal);

        assertNotNull(result);
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User("Test User", "test@example.com");
        when(userService.getUserById(1L)).thenReturn(user);

        User result = userService.getUserById(Long.valueOf("1"));

        assertNotNull(result);
        assertEquals("Test User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetActiveUser() {
        OAuth2User principal = mock(OAuth2User.class);

        User user = new User("Test User", "test@example.com");
        user.setUser_id(1L);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setUsername("Test User");
        when(userService.generateModel(user)).thenReturn(userModel);

        UserModel result = userController.getActiveUser(principal).getBody();

        assertNotNull(result);
        assertEquals("Test User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser() {
        OAuth2User principal = mock(OAuth2User.class);

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setUsername("Updated User");

        User updatedUser = new User("Updated User", "test@example.com");
        when(userService.generateModel(updatedUser)).thenReturn(userModel);
        when(userService.updateUser(userModel)).thenReturn(updatedUser);

        UserModel result = userController.updateUser(principal, userModel).getBody();

        assertNotNull(result);
        assertEquals("Updated User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }
}