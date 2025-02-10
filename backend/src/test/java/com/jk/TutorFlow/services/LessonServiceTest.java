package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLesson() {
        // Arrange
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // Act
        Optional<Lesson> result = lessonService.getLesson(lessonId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(lesson, result.get());
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    void testGetLessonNotFound() {
        // Arrange
        Long lessonId = 1L;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act
        Optional<Lesson> result = lessonService.getLesson(lessonId);

        // Assert
        assertFalse(result.isPresent());
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    void testGetLessonsByTeacherId() {
        // Arrange
        Long teacherId = 1L;
        Set<Lesson> lessons = Set.of(new Lesson(), new Lesson());
        when(lessonRepository.findAllByTeacherId(teacherId)).thenReturn(lessons);

        // Act
        Set<Lesson> result = lessonService.getLessonsByTeacherId(teacherId);

        // Assert
        assertEquals(2, result.size());
        verify(lessonRepository, times(1)).findAllByTeacherId(teacherId);
    }

    @Test
    void testGetLatestLessons() {
        // Arrange
        Long userId = 1L;
        Set<Lesson> lessons = Set.of(new Lesson(), new Lesson());
        when(lessonRepository.findLatest(userId)).thenReturn(lessons);

        // Act
        Set<Lesson> result = lessonService.getLatestLessons(userId);

        // Assert
        assertEquals(2, result.size());
        verify(lessonRepository, times(1)).findLatest(userId);
    }

    @Test
    void testDeleteLesson() {
        // Arrange
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // Act
        lessonService.deleteLesson(lessonId);

        // Assert
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    void testDeleteLessonNotFound() {
        // Arrange
        Long lessonId = 1L;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> lessonService.deleteLesson(lessonId));
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, never()).delete(any());
    }

    @Test
    void testAddLesson() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setStudentID(1L);
        model.setDate(LocalDate.now());
        Long teacherId = 1L;
        Set<File> files = Set.of(new File());

        User teacher = new User();
        teacher.setUser_id(teacherId);

        Student student = new Student();
        student.setStudent_id(model.getStudentID());

        Lesson lesson = new Lesson(model);
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setFiles(files);

        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(model.getStudentID())).thenReturn(Optional.of(student));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        // Act
        Lesson result = lessonService.addLesson(model, teacherId, files);

        // Assert
        assertNotNull(result);
        assertEquals(teacher, result.getTeacher());
        assertEquals(student, result.getStudent());
        assertEquals(files, result.getFiles());
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, times(1)).findById(model.getStudentID());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void testAddLessonTeacherNotFound() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setDate(LocalDate.now());
        model.setStudentID(1L);
        Long teacherId = 1L;
        Set<File> files = Set.of(new File());
        Lesson lesson = new Lesson(model);
        User teacher = new User();
        teacher.setUser_id(teacherId);
        lesson.setTeacher(teacher);
        lesson.setFiles(files);

        when(userRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> lessonService.addLesson(model, teacherId, files));
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, never()).findById(any());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void testAddLessonStudentNotFound() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setStudentID(1L);
        model.setDate(LocalDate.now());
        Long teacherId = 1L;
        Set<File> files = Set.of(new File());

        User teacher = new User();
        teacher.setUser_id(teacherId);

        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(model.getStudentID())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> lessonService.addLesson(model, teacherId, files));
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, times(1)).findById(model.getStudentID());
        verify(lessonRepository, never()).save(any());
    }
}