package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.net.URI;
import java.util.Map;

public class Task05 {
    public static void main(String[] args) {
        System.out.println("Reading data from DynamoDB table");
        
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("fakeaccess", "fakeaccess");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8001"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Students04"; 

        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();

            ScanResponse response = client.scan(scanRequest);

            System.out.println("\nScanned Items:");
            
            for (Map<String, AttributeValue> item : response.items()) {
                System.out.println("\nItem:");
                for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
                    String attributeName = entry.getKey();
                    AttributeValue attributeValue = entry.getValue();
                    System.out.println(attributeName + ": " + attributeValue);
                }
            }
            
            System.out.println("\nTotal items: " + response.count());

        } catch (Exception e) {
            System.err.println("Error scanning table: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}