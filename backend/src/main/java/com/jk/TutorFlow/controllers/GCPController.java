package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.services.GCPService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/storage/")
public class GCPController {

    @Autowired
    private GCPService GCPService;

    @Getter
    public static class FileRequest {
        private String objectName;
    }

    @PostMapping("download/")
    public void downloadFile(@RequestBody FileRequest request, HttpServletResponse response) throws IOException {
        GCPService.downloadFile(request.getObjectName(), response);
    }
}
