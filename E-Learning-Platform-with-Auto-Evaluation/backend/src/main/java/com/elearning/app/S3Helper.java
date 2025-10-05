package com.elearning.app;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3Helper {

    private AmazonS3 s3Client;
    private String bucketName;
    private boolean connected = false;

    public S3Helper() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
            String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
            String region = dotenv.get("AWS_REGION", "us-east-1");
            this.bucketName = dotenv.get("S3_BUCKET_NAME", "elearning-assignments");

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.fromName(region))
                    .build();

            if (!s3Client.doesBucketExistV2(bucketName)) {
                System.out.println("S3 bucket '" + bucketName + "' does not exist. Creating...");
                s3Client.createBucket(bucketName);
                System.out.println("✅ S3 bucket created: " + bucketName);
            }

            this.connected = true;
            System.out.println("S3 connection initialized successfully");
            System.out.println("   Region: " + region);
            System.out.println("   Bucket: " + bucketName);

        } catch (Exception e) {
            System.out.println("Error initializing S3: " + e.getMessage());
            e.printStackTrace();
            this.connected = false;
        }
    }

    public boolean isConnected() {
        return connected && s3Client != null;
    }

    /**
     * Upload a file to S3
     * @param file File to upload
     * @param key S3 key (path) where file will be stored
     * @return S3 object URL if successful, null otherwise
     */
    public String uploadFile(File file, String key) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot upload file");
            return null;
        }

        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
            s3Client.putObject(request);

            String objectUrl = s3Client.getUrl(bucketName, key).toString();
            System.out.println("File uploaded to S3: " + key);
            return objectUrl;

        } catch (Exception e) {
            System.out.println("Error uploading file to S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Upload a file with metadata
     */
    public String uploadFileWithMetadata(File file, String key, String studentId, String assignmentId) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot upload file");
            return null;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("studentId", studentId);
            metadata.addUserMetadata("assignmentId", assignmentId);
            metadata.addUserMetadata("uploadTime", java.time.LocalDateTime.now().toString());

            PutObjectRequest request = new PutObjectRequest(bucketName, key, file)
                    .withMetadata(metadata);

            s3Client.putObject(request);

            String objectUrl = s3Client.getUrl(bucketName, key).toString();
            System.out.println("File with metadata uploaded to S3: " + key);
            return objectUrl;

        } catch (Exception e) {
            System.out.println("Error uploading file with metadata to S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Download a file from S3
     */
    public InputStream downloadFile(String key) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot download file");
            return null;
        }

        try {
            S3Object s3Object = s3Client.getObject(bucketName, key);
            return s3Object.getObjectContent();

        } catch (Exception e) {
            System.out.println("Error downloading file from S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Download a file from S3 to a local temporary file
     * Returns the local File object for processing
     */
    public File downloadFileToTemp(String s3Key, String tempFileName) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot download file");
            return null;
        }

        try {
            File tempDir = new File("/tmp/elearning_downloads");
            tempDir.mkdirs();

            File tempFile = new File(tempDir, tempFileName);

            s3Client.getObject(new GetObjectRequest(bucketName, s3Key), tempFile);

            System.out.println("Downloaded file from S3 to temp: " + tempFile.getAbsolutePath());
            return tempFile;

        } catch (Exception e) {
            System.out.println("Error downloading file from S3 to temp: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Download a file from S3 and return its content as String
     */
    public String downloadFileAsString(String s3Key) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot download file");
            return null;
        }

        try {
            S3Object s3Object = s3Client.getObject(bucketName, s3Key);
            InputStream inputStream = s3Object.getObjectContent();

            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            System.out.println("Downloaded file content from S3: " + s3Key);
            return content.toString();

        } catch (Exception e) {
            System.out.println("Error downloading file content from S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete a file from S3
     */
    public boolean deleteFile(String key) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot delete file");
            return false;
        }

        try {
            s3Client.deleteObject(bucketName, key);
            System.out.println("File deleted from S3: " + key);
            return true;

        } catch (Exception e) {
            System.out.println("Error deleting file from S3: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * List all files in a folder (prefix)
     */
    public List<String> listFiles(String prefix) {
        List<String> fileKeys = new ArrayList<>();

        if (!isConnected()) {
            System.out.println("S3 not connected - cannot list files");
            return fileKeys;
        }

        try {
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucketName)
                    .withPrefix(prefix);

            ListObjectsV2Result result;
            do {
                result = s3Client.listObjectsV2(request);
                for (S3ObjectSummary summary : result.getObjectSummaries()) {
                    fileKeys.add(summary.getKey());
                }
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());

            System.out.println("Found " + fileKeys.size() + " files in S3 with prefix: " + prefix);

        } catch (Exception e) {
            System.out.println("Error listing files from S3: " + e.getMessage());
            e.printStackTrace();
        }

        return fileKeys;
    }

    /**
     * Get file metadata
     */
    public ObjectMetadata getFileMetadata(String key) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot get metadata");
            return null;
        }

        try {
            return s3Client.getObjectMetadata(bucketName, key);
        } catch (Exception e) {
            System.out.println("Error getting file metadata from S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a presigned URL for temporary file access
     * @param key S3 key
     * @param expirationMinutes How long the URL should be valid (in minutes)
     */
    public String generatePresignedUrl(String key, int expirationMinutes) {
        if (!isConnected()) {
            System.out.println("S3 not connected - cannot generate presigned URL");
            return null;
        }

        try {
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * expirationMinutes;
            expiration.setTime(expTimeMillis);

            String url = s3Client.generatePresignedUrl(bucketName, key, expiration).toString();
            System.out.println("Generated presigned URL valid for " + expirationMinutes + " minutes");
            return url;

        } catch (Exception e) {
            System.out.println("Error generating presigned URL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shutdown S3 client
     */
    public void shutdown() {
        if (s3Client != null) {
            s3Client.shutdown();
            System.out.println("S3 client shutdown");
        }
    }
}
