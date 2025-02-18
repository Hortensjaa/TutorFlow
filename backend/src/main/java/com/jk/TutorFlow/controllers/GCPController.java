package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.services.GCPService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GCPController {

    @Autowired
    private GCPService GCPService;

    @Getter
    public static class FileRequest {
        private String objectName;
    }

    @PostMapping("/api/storage/download/")
    public void downloadFile(@RequestBody FileRequest request, HttpServletResponse response) throws IOException {
        GCPService.downloadFile(request.getObjectName(), response);
    }
}
