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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
        List<Lesson> lessons = List.of(new Lesson(), new Lesson());
        Page<Lesson> lessonPage = new PageImpl<>(lessons);
        when(lessonRepository.findAllByTeacherId(teacherId, any(), any())).thenReturn(lessonPage);

        // Act
        Page<Lesson> result = lessonService.getLessonsByTeacherId(teacherId, any(), any(), any(), any(), any());

        // Assert
        assertEquals(2, result.getContent().size());
        verify(lessonRepository, times(1)).findAllByTeacherId(teacherId, any(), any());
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

    @Test
    void testUpdateLesson_Success() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setTopic("Updated Topic");
        model.setDate(LocalDate.of(2024, 2, 10));
        model.setDescription("Updated Description");
        model.setRate(4);
        model.setStudentID(1L);
        model.setFiles(new String[]{"file1.pdf", "file2.pdf"});

        Lesson lesson = new Lesson();
        lesson.setFiles(new HashSet<>(Set.of(new File("oldFile.pdf"))));

        Student student = new Student();
        student.setStudent_id(1L);

        Set<File> newFiles = Set.of(new File("newFile.pdf"));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Lesson updatedLesson = lessonService.updateLesson(model, lesson, newFiles);

        // Assert
        assertNotNull(updatedLesson);
        assertEquals("Updated Topic", updatedLesson.getTopic());
        assertEquals(Date.valueOf(LocalDate.of(2024, 2, 10)), updatedLesson.getDate());
        assertEquals("Updated Description", updatedLesson.getDescription());
        assertEquals(4, updatedLesson.getRate());
        assertEquals(student, updatedLesson.getStudent());
        assertTrue(updatedLesson.getFiles().stream().anyMatch(f -> f.getPath().equals("newFile.pdf")));
        verify(studentRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson_RemoveFiles() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setStudentID(1L);
        model.setDate(LocalDate.now());
        model.setFiles(new String[]{"file1.pdf"}); // Keeping only "file1.pdf"

        Lesson lesson = new Lesson();
        lesson.setFiles(new HashSet<>(Set.of(new File("file1.pdf"), new File("file2.pdf"))));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(new Student()));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Lesson updatedLesson = lessonService.updateLesson(model, lesson, null);

        // Assert
        assertEquals(1, updatedLesson.getFiles().size());
        assertTrue(updatedLesson.getFiles().stream().anyMatch(f -> f.getPath().equals("file1.pdf")));
        assertFalse(updatedLesson.getFiles().stream().anyMatch(f -> f.getPath().equals("file2.pdf")));
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson_AddNewFiles() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setStudentID(1L);
        model.setDate(LocalDate.now());
        model.setFiles(new String[]{"file1.pdf"});

        Lesson lesson = new Lesson();
        lesson.setFiles(new HashSet<>(Set.of(new File("file1.pdf"))));

        Set<File> newFiles = Set.of(new File("newFile.pdf"));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(new Student()));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Lesson updatedLesson = lessonService.updateLesson(model, lesson, newFiles);

        // Assert
        assertEquals(2, updatedLesson.getFiles().size());
        assertTrue(updatedLesson.getFiles().stream().anyMatch(f -> f.getPath().equals("file1.pdf")));
        assertTrue(updatedLesson.getFiles().stream().anyMatch(f -> f.getPath().equals("newFile.pdf")));
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson_StudentNotFound() {
        // Arrange
        LessonModel model = new LessonModel();
        model.setDate(LocalDate.now());
        model.setStudentID(999L);

        Lesson lesson = new Lesson();

        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> lessonService.updateLesson(model, lesson, null));
        verify(studentRepository, times(1)).findById(999L);
        verify(lessonRepository, never()).save(any());
    }

}