package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetStudents() {
        // Arrange
        Long teacherId = 1L;
        Set<Student> students = Set.of(new Student(), new Student());
        when(userRepository.findStudentsByTeacherId(teacherId)).thenReturn(students);

        // Act
        Set<Student> result = studentService.getStudents(teacherId);

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findStudentsByTeacherId(teacherId);
    }

    @Test
    void testAddStudent() {
        // Arrange
        Long teacherId = 1L;
        String studentName = "John Doe";
        User teacher = new User();
        Student student = new Student(studentName);

        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student result = studentService.addStudent(teacherId, studentName);

        // Assert
        assertNotNull(result);
        assertEquals(studentName, result.getName());
        assertEquals(teacher, result.getTeacher());
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(userRepository, times(1)).save(teacher);
    }

    @Test
    void testAddStudentTeacherNotFound() {
        // Arrange
        Long teacherId = 1L;
        String studentName = "John Doe";
        when(userRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.addStudent(teacherId, studentName));
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteStudent() {
        // Arrange
        Long teacherId = 1L;
        Long studentId = 1L;
        User teacher = new User();
        Student student = new Student();

        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Act
        studentService.deleteStudent(teacherId, studentId);

        // Assert
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student);
        verify(userRepository, times(1)).save(teacher);
    }

    @Test
    void testDeleteStudentTeacherNotFound() {
        // Arrange
        Long teacherId = 1L;
        Long studentId = 1L;
        when(userRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.deleteStudent(teacherId, studentId));
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, never()).findById(any());
        verify(studentRepository, never()).delete(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteStudentStudentNotFound() {
        // Arrange
        Long teacherId = 1L;
        Long studentId = 1L;
        User teacher = new User();
        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.deleteStudent(teacherId, studentId));
        verify(userRepository, times(1)).findById(teacherId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).delete(any());
        verify(userRepository, never()).save(any());
    }
}