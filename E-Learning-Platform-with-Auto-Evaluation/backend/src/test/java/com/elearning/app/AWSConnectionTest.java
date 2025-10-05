package com.elearning.app;

import java.io.File;
import java.io.FileWriter;

/**
 * Simple test class to verify AWS DynamoDB and S3 connectivity
 */
public class AWSConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== AWS Connection Test ===\n");

        System.out.println("1. Testing DynamoDB Connection...");
        DynamoDBHelper dynamoDBHelper = new DynamoDBHelper();

        if (dynamoDBHelper.isConnected()) {
            System.out.println(" DynamoDB is connected!\n");

            System.out.println("2. Testing DynamoDB Save Operation...");
            StudentScore testScore = new StudentScore(
                "TEST-" + System.currentTimeMillis(),
                "TEST001",
                "Test Student",
                85,
                "Test feedback",
                "test.txt"
            );
            dynamoDBHelper.saveScore(testScore);

            System.out.println("\n3. Testing DynamoDB Read Operation...");
            String scores = dynamoDBHelper.getAllScores();
            System.out.println(" Retrieved scores: " + scores.substring(0, Math.min(100, scores.length())) + "...");
        } else {
            System.out.println(" DynamoDB connection failed!\n");
        }

        System.out.println("\n4. Testing S3 Connection...");
        S3Helper s3Helper = new S3Helper();

        if (s3Helper.isConnected()) {
            System.out.println(" S3 is connected!\n");

            System.out.println("5. Testing S3 File Upload...");
            try {
                File tempFile = File.createTempFile("test-upload-", ".txt");
                FileWriter writer = new FileWriter(tempFile);
                writer.write("This is a test file for S3 upload.\nTesting E-Learning Platform integration.");
                writer.close();

                String s3Key = "test-files/test-" + System.currentTimeMillis() + ".txt";
                String uploadUrl = s3Helper.uploadFileWithMetadata(
                    tempFile,
                    s3Key,
                    "TEST001",
                    "TEST-ASSIGN-001"
                );

                if (uploadUrl != null) {
                    System.out.println("   File uploaded successfully!");
                    System.out.println("   S3 URL: " + uploadUrl);

                    System.out.println("\n6. Testing Presigned URL Generation...");
                    String presignedUrl = s3Helper.generatePresignedUrl(s3Key, 60);
                    if (presignedUrl != null) {
                        System.out.println("Presigned URL (valid for 60 min): " + presignedUrl.substring(0, Math.min(100, presignedUrl.length())) + "...");
                    }
                }

                tempFile.delete();

            } catch (Exception e) {
                System.out.println("   Error during S3 test: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("   S3 connection failed!\n");
        }

        System.out.println("\n=== Test Complete ===");
        System.out.println("\nIf both services are connected, your E-Learning Platform is ready to use AWS!");
        System.out.println("You can now run the main application: mvn exec:java -Dexec.mainClass=\"com.elearning.app.ELearningApp\"");

        dynamoDBHelper.shutdown();
        s3Helper.shutdown();
    }
}
