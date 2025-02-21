package com.jk.TutorFlow.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
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
    private OAuth2User principal;

    @Mock
    private Storage storage;
    @Mock
    private LessonService lessonService;
    @Mock
    private UserService userService;
    @Mock
    private StudentService studentService;
    @Mock
    private FileService fileService;

    @Mock
    private GCPService GCPService;

    @InjectMocks
    private LessonController lessonController;

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
        when(studentService.getStudents(1L)).thenReturn(Set.of(student));

        lesson = new Lesson();
        lesson.setLesson_id(1L);
        lesson.setTeacher(user);
        lesson.setStudent(student);
        lesson.setDate(new Date(System.currentTimeMillis()));
        lesson.setFiles(Collections.emptySet());
        lesson.setRate(5);
        when(lessonService.generateModel(any(Lesson.class))).thenReturn(new LessonModel());
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
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "dummy content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "dummy content".getBytes());

        MultipartFile[] files = {file1, file2};

        when(fileService.addFiles(any())).thenReturn(Collections.emptySet());
        when(lessonService.addLesson(any(), anyLong(), any())).thenReturn(new Lesson());

        LessonModel model = new LessonModel();

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
        lesson.setFiles(Collections.emptySet());
        lesson.setTeacher(otherUser);
        when(lessonService.getLesson(2L)).thenReturn(Optional.of(lesson));

        assertThrows(AccessDeniedException.class, () -> lessonController.deleteLesson(principal, "2"));
    }

    @Test
    void testDeleteLessonNotFound() {
        when(lessonService.getLesson(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lessonController.deleteLesson(principal, "2"));
    }

    @Test
    void testUpdateLesson() throws IOException {

        // Mock files
        Blob oldFileBlob = mock(Blob.class);
        when(storage.get(BlobId.of("tutorflow-storage", "oldFile.txt"))).thenReturn(oldFileBlob);
        when(oldFileBlob.exists()).thenReturn(true);

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        when(file1.getOriginalFilename()).thenReturn("file1.txt");
        when(file2.getOriginalFilename()).thenReturn("file2.txt");
        MultipartFile[] files = {file1, file2};

        // Mock existing lesson
        lesson.setFiles(Set.of(new File("oldFile.txt")));
        when(lessonService.getLesson(1L)).thenReturn(Optional.of(lesson));

        // Mock file service behavior: file existence check and adding files
        Set<File> newFiles = Set.of(new File("newFile1.txt"), new File("newFile2.txt"));
        when(fileService.addFiles(any())).thenReturn(newFiles);

        // Mock lesson update
        Lesson updatedLesson = new Lesson();
        updatedLesson.setLesson_id(1L);
        updatedLesson.setFiles(newFiles);
        when(lessonService.updateLesson(any(), any(), any())).thenReturn(updatedLesson);

        // Prepare lesson model
        LessonModel model = new LessonModel();
        model.setFiles(new String[]{"newFile1.txt", "newFile2.txt"});

        // Execute controller method
        ResponseEntity<LessonModel> response = lessonController.updateLesson(principal, "1", model, files);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testUpdateLessonUnauthorized() {
        User anotherUser = new User();
        anotherUser.setUser_id(99L);
        lesson.setTeacher(anotherUser);

        when(lessonService.getLesson(1L)).thenReturn(Optional.of(lesson));

        LessonModel model = new LessonModel();
        MultipartFile[] files = new MultipartFile[0];

        assertThrows(AccessDeniedException.class, () -> lessonController.updateLesson(principal, "1", model, files));
    }


}