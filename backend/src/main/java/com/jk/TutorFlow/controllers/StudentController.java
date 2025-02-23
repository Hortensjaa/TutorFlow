package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.StudentService;
import com.jk.TutorFlow.services.UserService;
import com.jk.TutorFlow.utils.PrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/students/")
public class StudentController {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PrincipalExtractor PrincipalExtractor;


    @GetMapping("all/")
    public ResponseEntity<List<StudentModel>> getStudents(@AuthenticationPrincipal OAuth2User principal) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        return ResponseEntity.ok().body(studentService.getStudents(user.getUser_id()));
    }

    @PostMapping("add/")
    public ResponseEntity<Map<String, Object>> addStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        UserModel existingTeacher = userService.getUserByEmail(userData.getEmail());
        String name = request.get("name");
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Name is required"));
        }
        return ResponseEntity.ok(Map.of("success", true, "student", studentService.addStudent(existingTeacher.getID(), name)));
    }

    @DeleteMapping("delete/")
    public void deleteStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody StudentModel studentModel
    ) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        UserModel existingTeacher = userService.getUserByEmail(userData.getEmail());
        studentService.deleteStudent(existingTeacher.getID(), studentModel.getID());
    }
}

