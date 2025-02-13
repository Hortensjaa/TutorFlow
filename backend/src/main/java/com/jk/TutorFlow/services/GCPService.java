package com.jk.TutorFlow.services;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


// source: https://github.com/sohamkamani/java-gcp-examples/blob/main/src/main/java/com/sohamkamani/storage/App.java
@Service
public class GCPService {
    private static final String projectId = "tutorflow-448111";
    private static final String bucketName = "tutorflow-storage";

    @NotNull
    private static String getWholeFileName(String userId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return userId + "/" + originalFilename;
    }

    public String[] uploadFiles(String userId, MultipartFile[] files) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        String[] publicUrls = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            String wholeFileName = getWholeFileName(userId, files[i]);
            BlobId blobId = BlobId.of(bucketName, wholeFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, files[i].getBytes());
            publicUrls[i] = wholeFileName;
        }

        return publicUrls;
    }

    public void downloadFile(String filePath, HttpServletResponse response) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, filePath);
        Blob blob = storage.get(blobId);

        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType(blob.getContentType());

        try (ReadChannel reader = blob.reader();
             OutputStream outputStream = response.getOutputStream()) {
            ByteBuffer buffer = ByteBuffer.allocate(10 * 100 * 1024);
            while (reader.read(buffer) > 0) {
                buffer.flip();
                outputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }
        }
    }


    public void deleteFile(String filePath) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, filePath);
        Blob blob = storage.get(blobId);

        if (blob == null) {
            System.out.println("File " + filePath + " does not exist in bucket " + bucketName);
            return;
        }

        blob.delete();
    }
}
