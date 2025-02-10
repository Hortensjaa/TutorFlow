package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.services.GCPService;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GCPController {

    @Getter
    public static class FileRequest {
        private String objectName;
    }

    @PostMapping("/api/storage/download")
    public void downloadFile(@RequestBody FileRequest request) throws IOException {
        GCPService.downloadFile(request.getObjectName());
    }
}
