package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.services.FileService;
import com.jk.TutorFlow.services.GCPService;
import com.jk.TutorFlow.services.LessonService;
import com.jk.TutorFlow.services.UserService;
import com.jk.TutorFlow.utils.PrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RestController
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private GCPService GCPService;
    @Autowired
    private PrincipalExtractor PrincipalExtractor;

    @GetMapping("/api/lessons/all/")
    public ResponseEntity<Page<LessonModel>> getAllLessons(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending,
            @RequestParam(defaultValue = "") Long studentId) {

        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Page<Lesson> lessons = lessonService.getLessonsByTeacherId(user.getUser_id(), studentId, page, size, sortBy, descending);
        Page<LessonModel> models = lessons.map(lessonService::generateModel);
        return ResponseEntity.ok().body(models);
    }

    @PostMapping("api/lessons/add/")
    public ResponseEntity<Lesson> addLesson(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestPart("lesson") LessonModel model,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        Long teacher_id = userService.getUserByEmail(principal.getAttribute("email")).getUser_id();
        Lesson lesson;
        if (files != null && files.length > 0) {
            String[] fileUrls = GCPService.uploadFiles(String.valueOf(teacher_id), files);
            Set<File> filesObjects = fileService.addFiles(fileUrls);
            lesson = lessonService.addLesson(model, teacher_id, filesObjects);
            fileService.updateFiles(lesson, filesObjects);
        } else {
            lesson = lessonService.addLesson(model, teacher_id, new HashSet<>());
        }
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping("/api/lessons/{id}/")
    public ResponseEntity<LessonModel> getLesson(
            @AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
        Optional<Lesson> optionalLesson = lessonService.getLesson(Long.valueOf(id));
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        if (optionalLesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        Lesson lesson = optionalLesson.get();
        if (!Objects.equals(user.getUser_id(), lesson.getTeacher().getUser_id())) {
            throw new AccessDeniedException("User not authorized to update lesson");
        }
        return new ResponseEntity<>(lessonService.generateModel(lesson), HttpStatus.OK);
    }

    @PutMapping("/api/lessons/{id}/edit/")
    public ResponseEntity<LessonModel> updateLesson(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable String id,
            @RequestPart("lesson") LessonModel model,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Optional<Lesson> optionalLesson = lessonService.getLesson(Long.valueOf(id));
        if (optionalLesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        Lesson lesson = optionalLesson.get();
        if (!Objects.equals(user.getUser_id(), lesson.getTeacher().getUser_id())) {
            throw new AccessDeniedException("User not authorized to update lesson");
        }

        // delete removed files
        lesson.getFiles().forEach(file -> {
            if (model.getFiles() == null || Arrays.stream(model.getFiles()).noneMatch(f -> f.equals(file.getPath()))) {
                GCPService.deleteFile(file.getPath());
            }
        });

        Lesson updatedLesson;

        // upload new files
        if (files != null && files.length > 0) {
            String[] fileUrls = GCPService.uploadFiles(String.valueOf(user.getUser_id()), files);
            Set<File> filesObjects = fileService.addFiles(fileUrls);
            fileService.updateFiles(lesson, filesObjects);

            // update lesson
            updatedLesson = lessonService.updateLesson(model, lesson, filesObjects);
        } else {
            updatedLesson = lessonService.updateLesson(model, lesson, new HashSet<>());
        }

        return new ResponseEntity<>(lessonService.generateModel(updatedLesson), HttpStatus.OK);
    }

    @DeleteMapping("/api/lessons/{id}/delete/")
    public void deleteLesson(@AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Optional<Lesson> optionalLesson = lessonService.getLesson(Long.valueOf(id));
        if (optionalLesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }

        Lesson lesson = optionalLesson.get();
        if (!Objects.equals(user.getUser_id(), lesson.getTeacher().getUser_id())) {
            throw new AccessDeniedException("User not authorized to delete lesson");
        }
        lesson.getFiles().stream().map(File::getPath).forEach(GCPService::deleteFile);
        lessonService.deleteLesson(Long.valueOf(id));
    }
}
