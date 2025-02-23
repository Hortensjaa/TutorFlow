package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.LessonMapper;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    private User teacher;
    private Student student;
    private Lesson lesson;
    private LessonModel lessonModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        teacher = new User();
        teacher.setUser_id(1L);

        student = new Student();
        student.setStudent_id(2L);

        lesson = new Lesson();
        lesson.setLesson_id(1L);
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setTopic("Math");
        lesson.setDate(Date.valueOf(LocalDate.now()));

        lessonModel = new LessonModel();
        lessonModel.setTopic("Math");
        lessonModel.setStudentID(1L);
        lessonModel.setDate(LocalDate.now());

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonMapper.toEntity(any(LessonModel.class))).thenReturn(lesson);
    }

    @Test
    public void testGetLessonSuccess() {
        Lesson result = lessonService.getLesson(1L, 1L);
        assertEquals(lesson, result);
    }

    @Test
    public void testGetLessonUnauthorized() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        try {
            lessonService.getLesson(1L, 99L);
            fail("Expected AccessDeniedException");
        } catch (AccessDeniedException e) {
            // Expected
        }
    }

    @Test
    public void testAddLessonSuccess() {
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = lessonService.addLesson(lessonModel, 1L, Set.of());
        assertNotNull(result);
        assertEquals("Math", result.getTopic());
    }

    @Test
    public void testDeleteLesson() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        lessonService.deleteLesson(1L);
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    public void testAddLessonWithFiles() {
        Set<File> files = Set.of(new File("file1.txt"), new File("file2.txt"));
        lessonModel.setFiles(new String[]{"file1.txt", "file2.txt"});
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = lessonService.addLesson(lessonModel, 1L, files);
        assertNotNull(result);
        assertEquals(2, result.getFiles().size());
    }

}
