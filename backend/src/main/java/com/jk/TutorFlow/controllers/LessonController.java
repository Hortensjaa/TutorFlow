package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    private List<LessonModel> getTaughtLessonsHelper(Long teacherId) {
        return lessonService.getLessonsByTeacherId(teacherId)
                .stream().map(LessonModel::new).collect(Collectors.toList());
    }

    private List<LessonModel> getAttendedLessonsHelper(Long studentId) {
        return lessonService.getLessonsByStudentId(studentId)
                .stream().map(LessonModel::new).collect(Collectors.toList());
    }

    @GetMapping("/api/lessons/teacher")
    public ResponseEntity<List<LessonModel>> getTaughtLessons(@AuthenticationPrincipal OAuth2User principal) {
        String user_id = principal.getAttribute("sub");
        if (user_id == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return new ResponseEntity<>(getTaughtLessonsHelper(Long.valueOf(user_id)), HttpStatus.OK);
    }

    @GetMapping("/api/lessons/student")
    public ResponseEntity<List<LessonModel>> getAttendedLessons(@AuthenticationPrincipal OAuth2User principal) {
        String user_id = principal.getAttribute("sub");
        if (user_id == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return new ResponseEntity<>(getAttendedLessonsHelper(Long.valueOf(user_id)), HttpStatus.OK);
    }

    @GetMapping("/api/lessons/")
    public ResponseEntity<List<LessonModel>> getAllLessons(@AuthenticationPrincipal OAuth2User principal) {
        String user_id = principal.getAttribute("sub");
        if (user_id == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        List<LessonModel> lessons = Stream.concat(
                getTaughtLessonsHelper(Long.valueOf(user_id)).stream(),
                getAttendedLessonsHelper(Long.valueOf(user_id)).stream()
        ).toList().stream().sorted((l1, l2) -> l2.getDate().compareTo(l1.getDate())).collect(Collectors.toList());
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
