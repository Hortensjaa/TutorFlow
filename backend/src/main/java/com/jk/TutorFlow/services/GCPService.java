package com.jk.TutorFlow.services;

import com.google.cloud.storage.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;


// source: https://github.com/sohamkamani/java-gcp-examples/blob/main/src/main/java/com/sohamkamani/storage/App.java
@Service
public class GCPService {
    private static final String projectId = "tutorflow-448111";
    private static final String bucketName = "tutorflow-storage";

    @NotNull
    private static String getWholeFileName(String userId, MultipartFile file, String uniqueId) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        String fileName = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        return userId + "/" + fileName + "--" + uniqueId + fileExtension;
    }

    public static String[] uploadFiles(String userId, MultipartFile[] files) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        String[] publicUrls = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            String uniqueId = UUID.randomUUID().toString();
            String wholeFileName = getWholeFileName(userId, files[i], uniqueId);
            BlobId blobId = BlobId.of(bucketName, wholeFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, files[i].getBytes());
            publicUrls[i] = wholeFileName;
        }

        return publicUrls;
    }

    public static void downloadFile(String filePath) {
        String userHome = System.getProperty("user.home");
        String downloadsFolder = userHome + "/Downloads/";
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        String downloadPath = downloadsFolder + fileName;

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, filePath);
        Blob blob = storage.get(blobId);

        blob.downloadTo(Paths.get(downloadPath));
    }


    public static void deleteFile(String filePath) {
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
