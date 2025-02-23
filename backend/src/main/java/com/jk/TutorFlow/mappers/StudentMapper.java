package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.models.StudentModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StudentMapper {
    public StudentModel toModel(Student entity, Optional<Lesson> lastLesson) {
        StudentModel model = new StudentModel();
        model.setID(entity.getStudent_id());
        model.setName(entity.getName());
        if (lastLesson.isPresent()) {
            model.setLastLesson(lastLesson.get().getDate().toLocalDate());
            model.setLastTopic(lastLesson.get().getTopic());
            model.setLastLessonId(lastLesson.get().getLesson_id());
        }
        model.setStudentModelClass("High School 1");
        return model;
    }

    public Student toEntity(StudentModel model) {
        Student entity = new Student();
        entity.setStudent_id(model.getID());
        entity.setName(model.getName());
        return entity;
    }
}
