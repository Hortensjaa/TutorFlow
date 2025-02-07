package com.jk.TutorFlow.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


// source: https://github.com/sohamkamani/java-gcp-examples/blob/main/src/main/java/com/sohamkamani/storage/App.java
@Service
public class GCPService {
    private static final String projectId = "tutorflow-448111";
    private static final String bucketName = "tutorflow-storage";

    public String[] uploadFiles(String userId, MultipartFile[] files) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        String[] publicUrls = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            String uniqueId = UUID.randomUUID().toString();
            String wholeFileName = getWholeFileName(userId, files[i], uniqueId);
            BlobId blobId = BlobId.of(bucketName, wholeFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, files[i].getBytes());
            String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + wholeFileName;
            publicUrls[i] = publicUrl;
        }

        return publicUrls;
    }

    @NotNull
    private static String getWholeFileName(String userId, MultipartFile file, String uniqueId) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        String fileName = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        return userId + "/" + fileName + uniqueId + "-" + fileExtension;
    }
}
