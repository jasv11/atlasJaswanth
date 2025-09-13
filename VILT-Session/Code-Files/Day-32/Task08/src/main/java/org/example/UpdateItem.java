package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UpdateItem {
    public static void main(String[] args) {
        
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("fakeaccess", "fakeaccess");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8001"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Students04";

        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("ID", AttributeValue.builder().n("1001").build());

            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("Address", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s("Delhi").build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .attributeUpdates(updates)
                    .returnValues(ReturnValue.ALL_NEW)
                    .build();

            UpdateItemResponse response = client.updateItem(request);

            if (response.attributes() != null) {
                for (Map.Entry<String, AttributeValue> entry : response.attributes().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

        } catch (Exception e) {
            System.err.println("Error updating item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }

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
            e.printStackTrace();
        }
    }
}