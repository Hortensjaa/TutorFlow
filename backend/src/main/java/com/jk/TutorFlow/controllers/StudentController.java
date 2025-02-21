package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
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
public class StudentController {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private PrincipalExtractor PrincipalExtractor;


    @GetMapping("/api/students/all/")
    public ResponseEntity<List<StudentModel>> getStudents(@AuthenticationPrincipal OAuth2User principal) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        User entity = userService.getUserByEmail(userData.getEmail());
        List<StudentModel> students =  studentService.getStudents(entity.getUser_id()).stream().map(e -> studentService.generateModel(e)).toList();
        return ResponseEntity.ok().body(students);
    }

    @PostMapping("/api/students/add/")
    public ResponseEntity<Map<String, Object>> addStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        User existingTeacher = userService.getUserByEmail(userData.getEmail());
        String name = request.get("name");
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Name is required"));
        }

        Student student = studentService.addStudent(existingTeacher.getUser_id(), name);
        return ResponseEntity.ok(Map.of("success", true, "student", studentService.generateModel(student)));
    }

    @DeleteMapping("/api/students/delete/")
    public void deleteStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody StudentModel studentModel
    ) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        User existingTeacher = userService.getUserByEmail(userData.getEmail());
        System.out.println(studentModel.getID());
        System.out.println(studentModel.getName());
        studentService.deleteStudent(existingTeacher.getUser_id(), studentModel.getID());
    }
}

