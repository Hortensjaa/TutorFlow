package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.StudentMapper;
import com.jk.TutorFlow.models.StudentModel;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private User teacher;
    private Student student;
    private StudentModel studentModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        teacher = new User();
        teacher.setUser_id(1L);

        student = new Student();
        student.setStudent_id(2L);
        student.setName("John Doe");

        studentModel = new StudentModel();
        studentModel.setName("John Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        when(studentMapper.toModel(any(Student.class), any())).thenReturn(studentModel);
    }

    @Test
    public void testGetStudentsSuccess() {
        when(studentRepository.findStudentsByTeacherId(1L)).thenReturn(List.of(student));

        List<StudentModel> result = studentService.getStudents(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testAddStudentSuccess() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentModel result = studentService.addStudent(1L, "John Doe");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testDeleteStudentSuccess() {
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L, 2L);

        verify(studentRepository, times(1)).delete(student);
        verify(userRepository, times(1)).save(teacher);
    }

    @Test
    public void testDeleteStudentStudentNotFound() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        try {
            studentService.deleteStudent(1L, 2L);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeleteStudentTeacherNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            studentService.deleteStudent(1L, 2L);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}
