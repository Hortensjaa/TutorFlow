package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/api/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons() {
        System.out.println("Getting all lessons");
        List<LessonModel> lessons = lessonService.getLessons()
                .stream().map(LessonModel::new).collect(Collectors.toList());
        System.out.println(lessons);
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }


    @GetMapping("/api/lesson/{id}")
    public ResponseEntity<LessonModel> getLesson(@PathVariable String id) {
        Optional<Lesson> lesson = lessonService.getLesson(Long.valueOf(id));
        return lesson.map(
                        value -> new ResponseEntity<>(new LessonModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
