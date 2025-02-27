package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.models.TagModel;
import com.jk.TutorFlow.services.*;
import com.jk.TutorFlow.utils.PrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/lessons/")
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TagService tagService;
    @Autowired
    private GCPService GCPService;
    @Autowired
    private PrincipalExtractor PrincipalExtractor;

    @GetMapping("all/")
    public ResponseEntity<Page<LessonModel>> getAllLessons(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending,
            @RequestParam(defaultValue = "") Long studentId) {

        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        return ResponseEntity.ok().body(lessonService
                .getLessonsByTeacherId(user.getUser_id(), studentId, page, size, sortBy, descending));
    }

    @PostMapping("add/")
    public ResponseEntity<Lesson> addLesson(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestPart("lesson") LessonModel model,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        Long teacher_id = userService.getUserByEmail(principal.getAttribute("email")).getID();
        Lesson lesson;
        System.out.println(model);
        if (files != null && files.length > 0) {
            String[] fileUrls = GCPService.uploadFiles(String.valueOf(teacher_id), files);
            Set<File> filesObjects = fileService.addFiles(fileUrls);
            lesson = lessonService.addLesson(model, teacher_id, filesObjects);
            fileService.updateFiles(lesson, filesObjects);

        } else {
            lesson = lessonService.addLesson(model, teacher_id, new HashSet<>());
        }
        tagService.addTagsToLesson(
                Set.of(model.getTags()).stream().map(TagModel::getID).collect(Collectors.toSet()),
                lesson.getLesson_id());
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping("{id}/")
    public ResponseEntity<LessonModel> getLesson(
            @AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        return new ResponseEntity<>(lessonService.getLessonModel(Long.valueOf(id), user.getUser_id()), HttpStatus.OK);
    }

    @PutMapping("{id}/edit/")
    public ResponseEntity<LessonModel> updateLesson(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable String id,
            @RequestPart("lesson") LessonModel model,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Lesson lesson = lessonService.getLesson(Long.valueOf(id), user.getUser_id());

        // delete removed files
        lesson.getFiles().forEach(file -> {
            if (model.getFiles() == null || Arrays.stream(model.getFiles()).noneMatch(f -> f.equals(file.getPath()))) {
                GCPService.deleteFile(file.getPath());
            }
        });

        LessonModel updatedLesson;

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

        tagService.addTagsToLesson(
                Set.of(model.getTags()).stream().map(TagModel::getID).collect(Collectors.toSet()),
                lesson.getLesson_id());

        return new ResponseEntity<>(updatedLesson, HttpStatus.OK);
    }

    @DeleteMapping("{id}/delete/")
    public void deleteLesson(@AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Lesson lesson = lessonService.getLesson(Long.valueOf(id), user.getUser_id());
        lesson.getFiles().stream().map(File::getPath).forEach(GCPService::deleteFile);
        lessonService.deleteLesson(Long.valueOf(id));
    }

    @GetMapping("upcoming/")
    public ResponseEntity<List<LessonModel>> upcomingLessons(@AuthenticationPrincipal OAuth2User principal) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        List<LessonModel> lessons = lessonService.upcomingLessons(user.getUser_id());
        return ResponseEntity.ok().body(lessons);
    }
}
