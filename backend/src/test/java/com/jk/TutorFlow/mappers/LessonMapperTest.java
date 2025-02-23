package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LessonMapperTest {

    private LessonMapper lessonMapper;

    @Before
    public void setUp() {
        lessonMapper = new LessonMapper();
    }

    @Test
    public void testToModel() {
        // Setup mock Lesson
        User teacher = new User();
        teacher.setUsername("teacher@example.com");

        Student student = new Student();
        student.setName("Student1");
        student.setStudent_id(1L);

        File file = new File();
        file.setPath("file1.txt");

        Lesson lesson = new Lesson();
        lesson.setLesson_id(1L);
        lesson.setTopic("Math");
        lesson.setDate(Date.valueOf(LocalDate.now()));
        lesson.setDescription("Math lesson description");
        lesson.setRate(5);
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setFiles(Set.of(file));

        // Map Lesson to LessonModel
        LessonModel lessonModel = lessonMapper.toModel(lesson);

        // Assertions
        assertNotNull(lessonModel);
        assertEquals(1L, lessonModel.getID());
        assertEquals("Math", lessonModel.getTopic());
        assertEquals(LocalDate.now(), lessonModel.getDate());
        assertEquals("Math lesson description", lessonModel.getDescription());
        assertNotNull(lessonModel.getRate());
        assertEquals("Student1", lessonModel.getStudent());
        assertEquals(1L, lessonModel.getStudentID());
        assertEquals("teacher@example.com", lessonModel.getTeacher());
        assertEquals(1, lessonModel.getFiles().length);
        assertEquals("file1.txt", lessonModel.getFiles()[0]);
    }

    @Test
    public void testToEntity() {
        // Setup mock LessonModel
        LessonModel lessonModel = new LessonModel();
        lessonModel.setTopic("Math");
        lessonModel.setDescription("Math lesson description");
        lessonModel.setDate(LocalDate.now());
        lessonModel.setRate(5);

        // Map LessonModel to Lesson
        Lesson lesson = lessonMapper.toEntity(lessonModel);

        // Assertions
        assertNotNull(lesson);
        assertEquals("Math", lesson.getTopic());
        assertEquals("Math lesson description", lesson.getDescription());
        assertEquals(Date.valueOf(LocalDate.now()), lesson.getDate());
        assertNotNull(lessonModel.getRate());
    }
}
