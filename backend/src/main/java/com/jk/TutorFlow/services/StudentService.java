package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private LessonRepository lessonRepository;

    public StudentModel generateModel(Student entity) {
        Optional<Lesson> lastLesson = lessonRepository.findMostRecentLessonByStudentId(entity.getStudent_id());

        StudentModel model = new StudentModel();
        model.setID(entity.getStudent_id());
        model.setName(entity.getName());
        if (lastLesson.isPresent()) {
            model.setLastLesson(lastLesson.get().getDate().toLocalDate());
            model.setLastTopic(lastLesson.get().getTopic());
        }
        model.setStudentModelClass("High School 1"); // todo: classes
        return model;
    }
}
