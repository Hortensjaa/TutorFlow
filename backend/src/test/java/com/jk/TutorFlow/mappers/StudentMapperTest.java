package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.models.StudentModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    void testToModelWithLesson() {
        // Create a student entity
        Student student = new Student();
        student.setStudent_id(1L);
        student.setName("John Doe");

        // Create a lesson entity
        Lesson lesson = new Lesson();
        lesson.setLesson_id(100L);
        lesson.setTopic("Math");
        lesson.setDate(Date.valueOf(LocalDate.now()));

        // Wrap the lesson in an Optional
        Optional<Lesson> optionalLesson = Optional.of(lesson);

        // Map Student with Optional lesson
        StudentModel studentModel = studentMapper.toModel(student, optionalLesson);

        // Assert that the fields are mapped correctly
        assertNotNull(studentModel);
        assertEquals(1L, studentModel.getID()); // ID should match
        assertEquals("John Doe", studentModel.getName()); // Name should match
        assertEquals(LocalDate.now(), studentModel.getLastLesson()); // Last lesson date should match
        assertEquals("Math", studentModel.getLastTopic()); // Topic should match
        assertEquals(100L, studentModel.getLastLessonId()); // Lesson ID should match
        assertEquals("High School 1", studentModel.getStudentModelClass()); // Default class should be set
    }

    @Test
    void testToModelWithoutLesson() {
        // Create a student entity
        Student student = new Student();
        student.setStudent_id(2L);
        student.setName("Jane Doe");

        // Wrap an empty Optional to simulate no last lesson
        Optional<Lesson> optionalLesson = Optional.empty();

        // Map Student with empty Optional (no lesson)
        StudentModel studentModel = studentMapper.toModel(student, optionalLesson);

        // Assert that the fields are mapped correctly
        assertNotNull(studentModel);
        assertEquals(2L, studentModel.getID()); // ID should match
        assertEquals("Jane Doe", studentModel.getName()); // Name should match
        assertNull(studentModel.getLastLesson()); // Last lesson should be null
        assertNull(studentModel.getLastTopic()); // Last topic should be null
        assertEquals("High School 1", studentModel.getStudentModelClass()); // Default class should be set
    }

    @Test
    void testToEntity() {
        // Create a StudentModel for testing
        StudentModel studentModel = new StudentModel();
        studentModel.setID(3L);
        studentModel.setName("Tom Smith");

        // Convert the StudentModel to Student entity
        Student studentEntity = studentMapper.toEntity(studentModel);

        // Assert that the conversion works correctly
        assertNotNull(studentEntity);
        assertEquals(3L, studentEntity.getStudent_id()); // ID should match
        assertEquals("Tom Smith", studentEntity.getName()); // Name should match
    }
}
