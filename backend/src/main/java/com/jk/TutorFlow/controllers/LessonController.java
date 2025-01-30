package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.LessonService;
import com.jk.TutorFlow.services.UserService;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserService userService;

    private List<LessonModel> getTaughtLessonsHelper(Long teacherId) {
        return lessonService.getLessonsByTeacherId(teacherId)
                .stream().map(LessonModel::new).collect(Collectors.toList());
    }

    private List<LessonModel> getAttendedLessonsHelper(Long studentId) {
        return lessonService.getLessonsByStudentId(studentId)
                .stream().map(LessonModel::new).collect(Collectors.toList());
    }

    private List<LessonModel> getAllLessonsHelper(Long user_id) {
        if (!userService.isTeacher(user_id)) {
            return getAttendedLessonsHelper(user_id);
        }
        if (!userService.isStudent(user_id)) {
            return getTaughtLessonsHelper(user_id);
        }
        return lessonService.getLessonsByUserId(user_id).stream().map(LessonModel::new).collect(Collectors.toList());
    }

    private User getUser(@AuthenticationPrincipal OAuth2User principal) {
        String user_email = principal.getAttribute("email");
        if (user_email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = userService.getUserByEmail(user_email);
        if (user == null) {
            throw new AccessDeniedException("User not found");
        }
        return user;
    }

    @GetMapping("/api/lessons/teacher")
    public ResponseEntity<List<LessonModel>> getTaughtLessons(@AuthenticationPrincipal OAuth2User principal) {
        User user = getUser(principal);
        return new ResponseEntity<>(getTaughtLessonsHelper(user.getUser_id()), HttpStatus.OK);
    }

    @GetMapping("/api/lessons/student")
    public ResponseEntity<List<LessonModel>> getAttendedLessons(@AuthenticationPrincipal OAuth2User principal) {
        User user = getUser(principal);
        return new ResponseEntity<>(getAttendedLessonsHelper(user.getUser_id()), HttpStatus.OK);
    }

    @GetMapping("/api/lessons/all")
    public ResponseEntity<List<LessonModel>> getAllLessons(@AuthenticationPrincipal OAuth2User principal) {
        User user = getUser(principal);
        return new ResponseEntity<>(getAllLessonsHelper(user.getUser_id()), HttpStatus.OK);
    }

    @GetMapping("/api/lessons/latest")
    public ResponseEntity<List<LessonModel>> getLatestLessons(@AuthenticationPrincipal OAuth2User principal) {
        User user = getUser(principal);
        return new ResponseEntity<>(
                lessonService.getLatestLessons(user.getUser_id())
                        .stream().map(LessonModel::new).collect(Collectors.toList()),
                HttpStatus.OK);
    }


    @GetMapping("/api/lesson/{id}")
    public ResponseEntity<LessonModel> getLesson(@PathVariable String id) {
        Optional<Lesson> lesson = lessonService.getLesson(Long.valueOf(id));
        return lesson.map(
                        value -> new ResponseEntity<>(new LessonModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
