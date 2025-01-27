package com.jk.TutorFlow.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @GetMapping("/api/success")
    public ResponseEntity<String> getAllLessons() {
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }
}
