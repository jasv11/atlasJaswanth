package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DeleteItem {
    public static void main(String[] args) {
        System.out.println("Deleting item from DynamoDB table");
        
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("fakeaccess", "fakeaccess");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8001"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Students04";

        try {
            System.out.println("\nTable contents before deletion:");
            printTableContents(client, tableName);

            Map<String, AttributeValue> key = new HashMap<>();
            key.put("ID", AttributeValue.builder().n("1002").build());

            DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .returnValues(ReturnValue.ALL_OLD)  
                    .build();

            DeleteItemResponse deleteResponse = client.deleteItem(deleteRequest);

            System.out.println("\nDeleted item attributes:");
            if (deleteResponse.attributes() != null) {
                for (Map.Entry<String, AttributeValue> entry : deleteResponse.attributes().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

            System.out.println("\nTable contents after deletion:");
            printTableContents(client, tableName);

        } catch (Exception e) {
            System.err.println("Error deleting item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    private static void printTableContents(DynamoDbClient client, String tableName) {
        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();

            ScanResponse scanResponse = client.scan(scanRequest);

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                System.out.println("\nItem:");
                for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        } catch (Exception e) {
            System.err.println("Error scanning table: " + e.getMessage());
        }
    }
}