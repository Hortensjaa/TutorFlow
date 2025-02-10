package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.controllers.UserController;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        UserModel result = userController.getActiveUser(principal);

        assertNotNull(result);
        assertEquals("Test User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetStudents() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        User user = new User("Test User", "test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        Student student = new Student("Student Name");
        student.setStudent_id(1L);
        user.setStudents(Set.of(student));
        student.setTeacher(user);

        Student not_my_student = new Student("NonStudent Name");
        not_my_student.setStudent_id(2L);
        when(userService.getStudents(user.getUser_id())).thenReturn(Set.of(student));

        List<StudentModel> result = userController.getStudents(principal);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Student Name", result.get(0).getName());
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

        User result = userController.updateUser(principal, userModel);

        assertNotNull(result);
        assertEquals("Updated User", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testAddStudentByTeacher() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        User user = new User("Test User", "test@example.com");
        user.setUser_id(1L);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        Student student = new Student("New Student");
        student.setStudent_id(2L);
        when(userService.addStudent(user.getUser_id(), "New Student")).thenReturn(student);

        ResponseEntity<Map<String, Object>> result =
                userController.addStudentByTeacher(principal, Map.of("name", "New Student"));

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue((Boolean) result.getBody().get("success"));
        assertEquals("New Student", ((StudentModel) result.getBody().get("student")).getName());
    }

    @Test
    void testDeleteStudentByTeacher() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        User user = new User("Test User", "test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        StudentModel studentModel = new StudentModel();
        studentModel.setID(1L);
        studentModel.setName("Student Name");

        userController.deleteStudentByTeacher(principal, studentModel);

        verify(userService, times(1)).deleteStudent(user.getUser_id(), studentModel.getID());
    }
}