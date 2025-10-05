package com.elearning.app;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;


public class SetupDynamoDB {

    public static void main(String[] args) {
        System.out.println("=== DynamoDB Setup Script ===\n");

        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
            String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
            String region = dotenv.get("AWS_REGION", "us-east-1");

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.fromName(region))
                    .build();

            System.out.println("Connected to DynamoDB in region: " + region + "\n");

            System.out.println("Creating Students table...");
            createStudentsTable(dynamoDB);

            System.out.println("\nCreating StudentScores table...");
            createStudentScoresTable(dynamoDB);

            System.out.println("\nCreating SystemLogs table...");
            createSystemLogsTable(dynamoDB);

            System.out.println("\nCreating Assignments table...");
            createAssignmentsTable(dynamoDB);

            System.out.println("\n=== Setup Complete ===");
            System.out.println("Your DynamoDB tables are ready!");
            System.out.println("\nYou can now run the application:");
            System.out.println("mvn exec:java -Dexec.mainClass=\"com.elearning.app.ELearningApp\"");

            dynamoDB.shutdown();

        } catch (Exception e) {
            System.out.println("Error during setup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createStudentsTable(AmazonDynamoDB dynamoDB) {
        try {
            try {
                DescribeTableRequest describeRequest = new DescribeTableRequest()
                        .withTableName("Students");
                dynamoDB.describeTable(describeRequest);
                System.out.println("   Table 'Students' already exists. Skipping...");
                return;
            } catch (ResourceNotFoundException e) {
               
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName("Students")
                    .withKeySchema(
                            new KeySchemaElement("studentId", KeyType.HASH)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("studentId", ScalarAttributeType.S)
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST); 

            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("   Table 'Students' created successfully!");
            System.out.println("   Table ARN: " + result.getTableDescription().getTableArn());

            // Wait for table to become active
            System.out.println("   Waiting for table to become active...");
            waitForTableToBecomeActive(dynamoDB, "Students");
            System.out.println("   Table is now active!");

        } catch (Exception e) {
            System.out.println("   Error creating Students table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createStudentScoresTable(AmazonDynamoDB dynamoDB) {
        try {
            try {
                DescribeTableRequest describeRequest = new DescribeTableRequest()
                        .withTableName("StudentScores");
                dynamoDB.describeTable(describeRequest);
                System.out.println("   Table 'StudentScores' already exists. Skipping...");
                return;
            } catch (ResourceNotFoundException e) {
                
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName("StudentScores")
                    .withKeySchema(
                            new KeySchemaElement("assignmentId", KeyType.HASH)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("assignmentId", ScalarAttributeType.S)
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST);

            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("   Table 'StudentScores' created successfully!");
            System.out.println("   Table ARN: " + result.getTableDescription().getTableArn());

            // Wait for table to become active
            System.out.println("   Waiting for table to become active...");
            waitForTableToBecomeActive(dynamoDB, "StudentScores");
            System.out.println("   Table is now active!");

        } catch (Exception e) {
            System.out.println("   Error creating StudentScores table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createSystemLogsTable(AmazonDynamoDB dynamoDB) {
        try {
            // Check if table already exists
            try {
                DescribeTableRequest describeRequest = new DescribeTableRequest()
                        .withTableName("SystemLogs");
                dynamoDB.describeTable(describeRequest);
                System.out.println("   Table 'SystemLogs' already exists. Skipping...");
                return;
            } catch (ResourceNotFoundException e) {
               
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName("SystemLogs")
                    .withKeySchema(
                            new KeySchemaElement("logId", KeyType.HASH)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("logId", ScalarAttributeType.S)
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST); 

            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("   Table 'SystemLogs' created successfully!");
            System.out.println("   Table ARN: " + result.getTableDescription().getTableArn());

            // Wait for table to become active
            System.out.println("   Waiting for table to become active...");
            waitForTableToBecomeActive(dynamoDB, "SystemLogs");
            System.out.println("   Table is now active!");

        } catch (Exception e) {
            System.out.println("   Error creating SystemLogs table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createAssignmentsTable(AmazonDynamoDB dynamoDB) {
        try {
            try {
                DescribeTableRequest describeRequest = new DescribeTableRequest()
                        .withTableName("Assignments");
                dynamoDB.describeTable(describeRequest);
                System.out.println("   Table 'Assignments' already exists. Skipping...");
                return;
            } catch (ResourceNotFoundException e) {
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName("Assignments")
                    .withKeySchema(
                            new KeySchemaElement("assignmentId", KeyType.HASH)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("assignmentId", ScalarAttributeType.S)
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST); 

            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("   Table 'Assignments' created successfully!");
            System.out.println("   Table ARN: " + result.getTableDescription().getTableArn());

            System.out.println("   Waiting for table to become active...");
            waitForTableToBecomeActive(dynamoDB, "Assignments");
            System.out.println("   Table is now active!");

        } catch (Exception e) {
            System.out.println("   Error creating Assignments table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void waitForTableToBecomeActive(AmazonDynamoDB dynamoDB, String tableName) {
        int maxAttempts = 20;
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                Thread.sleep(2000);

                DescribeTableRequest request = new DescribeTableRequest()
                        .withTableName(tableName);
                DescribeTableResult result = dynamoDB.describeTable(request);
                String status = result.getTable().getTableStatus();

                System.out.println("      Status: " + status);

                if ("ACTIVE".equals(status)) {
                    return;
                }

                attempts++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for table to become active", e);
            }
        }

        throw new RuntimeException("Table did not become active within expected time");
    }
}
