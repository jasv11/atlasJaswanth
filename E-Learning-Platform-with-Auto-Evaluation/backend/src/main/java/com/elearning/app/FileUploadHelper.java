package com.elearning.app;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUploadHelper {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/elearning/";

    public FileUploadHelper() {
        try {
            Files.createDirectories(Paths.get(TEMP_DIR));
        } catch (IOException e) {
            System.out.println("Error creating temp directory: " + e.getMessage());
        }
    }

    public File saveTemporaryFile(Part filePart, String assignmentId) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String tempFileName = assignmentId + extension;

        File tempFile = new File(TEMP_DIR + tempFileName);

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

    public void cleanupTempFiles() {
        File tempDir = new File(TEMP_DIR);
        if (tempDir.exists()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }
}