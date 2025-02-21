package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.services.StudentService;
import com.jk.TutorFlow.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StudentControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        StudentModel studentModel = new StudentModel();
        studentModel.setName("Student Name");
        when(studentService.generateModel(any())).thenReturn(studentModel);
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

        List<StudentModel> result = studentController.getStudents(principal).getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Student Name", result.get(0).getName());
    }

    @Test
    void testAddStudentByTeacher() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test User");

        User user = new User("Test User", "test@example.com");
        user.setUser_id(1L);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        Student student = new Student("Student Name");
        student.setStudent_id(2L);
        when(userService.addStudent(user.getUser_id(), "Student Name")).thenReturn(student);

        ResponseEntity<Map<String, Object>> result =
                studentController.addStudentByTeacher(principal, Map.of("name", "Student Name"));

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue((Boolean) result.getBody().get("success"));
        assertEquals("Student Name", ((StudentModel) result.getBody().get("student")).getName());
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

        studentController.deleteStudentByTeacher(principal, studentModel);

        verify(studentService, times(1)).deleteStudent(user.getUser_id(), studentModel.getID());
    }
}
