package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.GCPService;
import com.jk.TutorFlow.services.LessonService;
import com.jk.TutorFlow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserService userService;
    @Autowired
    private GCPService gcpService;

    private List<LessonModel> getAllLessonsHelper(Long teacherId) {
        return lessonService.getLessonsByTeacherId(teacherId)
                .stream().map(LessonModel::new).collect(Collectors.toList());
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

    @PostMapping("api/lessons/add")
    public ResponseEntity<Lesson> addLesson(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestPart("lesson") LessonModel model,
            @RequestPart("files") MultipartFile[] files
    ) throws IOException {
        Long teacher_id = userService.getUserByEmail(principal.getAttribute("email")).getUser_id();
        String[] fileUrls = gcpService.uploadFiles(String.valueOf(teacher_id), files);
        Lesson lesson = lessonService.addLesson(model, teacher_id, fileUrls);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping("/api/lessons/{id}")
    public ResponseEntity<LessonModel> getLesson(@PathVariable String id) {
        Optional<Lesson> lesson = lessonService.getLesson(Long.valueOf(id));
        return lesson.map(
                        value -> new ResponseEntity<>(new LessonModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/api/lessons/{id}/delete")
    public void deleteLesson(@AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
        User user = getUser(principal);
        Optional<Lesson> optionalLesson = lessonService.getLesson(Long.valueOf(id));
        if (optionalLesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }

        Lesson lesson = optionalLesson.get();
        if (!Objects.equals(user.getUser_id(), lesson.getTeacher().getUser_id())) {
            throw new AccessDeniedException("User not authorized to delete lesson");
        }
        lessonService.deleteLesson(Long.valueOf(id));
    }
}
