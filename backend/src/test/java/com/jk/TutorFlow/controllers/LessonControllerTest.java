package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.FileService;
import com.jk.TutorFlow.services.GCPService;
import com.jk.TutorFlow.services.LessonService;
import com.jk.TutorFlow.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private LessonController lessonController;

    @Mock
    private OAuth2User principal;

    private User user;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUser_id(1L);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        student = new Student();
        student.setStudent_id(1L);
        student.setTeacher(user);
        when(userService.getStudents(1L)).thenReturn(Set.of(student));

        lesson = new Lesson();
        lesson.setLesson_id(1L);
        lesson.setTeacher(user);
        lesson.setStudent(student);
        lesson.setDate(new Date(System.currentTimeMillis()));
        when(lessonService.getLessonsByTeacherId(1L)).thenReturn(Set.of(lesson));
        when(lessonService.getLesson(1L)).thenReturn(Optional.of(lesson));

    }

    @Test
    void testGetAllLessons() {
        ResponseEntity<List<LessonModel>> response = lessonController.getAllLessons(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetLatestLessons() {
        user.setTaught_lessons(Set.of(lesson));
        when(lessonService.getLatestLessons(1L)).thenReturn(Set.of(lesson));

        ResponseEntity<List<LessonModel>> response = lessonController.getLatestLessons(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAddLesson() throws IOException {
        when(GCPService.uploadFiles(anyString(), any())).thenReturn(new String[]{"url1", "url2"});
        when(fileService.addFiles(any())).thenReturn(Collections.emptySet());
        when(lessonService.addLesson(any(), anyLong(), any())).thenReturn(new Lesson());

        LessonModel model = new LessonModel();
        MultipartFile[] files = new MultipartFile[0];

        ResponseEntity<Lesson> response = lessonController.addLesson(principal, model, files);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetLesson() {
        ResponseEntity<LessonModel> response = lessonController.getLesson(principal, "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetLessonNotFound() {
        when(lessonService.getLesson(2L)).thenReturn(Optional.empty());

        ResponseEntity<LessonModel> response = lessonController.getLesson(principal, "2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteLesson() {
        lessonController.deleteLesson(principal, "1");

        verify(lessonService, times(1)).deleteLesson(1L);
    }

    @Test
    void testDeleteLessonUnauthorized() {
        User otherUser = new User();
        otherUser.setUser_id(2L);
        Lesson lesson = new Lesson();
        lesson.setLesson_id(2L);
        lesson.setTeacher(otherUser);
        when(lessonService.getLesson(2L)).thenReturn(Optional.of(lesson));

        assertThrows(AccessDeniedException.class, () -> lessonController.deleteLesson(principal, "2"));
    }

    @Test
    void testDeleteLessonNotFound() {
        when(lessonService.getLesson(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lessonController.deleteLesson(principal, "2"));
    }
}