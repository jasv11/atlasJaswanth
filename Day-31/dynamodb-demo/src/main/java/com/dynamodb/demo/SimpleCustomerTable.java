import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.net.URI;

public class SimpleCustomerTable {
    public static void main(String[] args) {
        try {
            DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("dummy", "dummy")))
                .region(Region.US_WEST_2)
                .build();

            CreateTableRequest request = CreateTableRequest.builder()
                .tableName("Customer")
                .keySchema(
                    KeySchemaElement.builder()
                        .attributeName("ID")
                        .keyType(KeyType.HASH)
                        .build(),
                    KeySchemaElement.builder()
                        .attributeName("No")
                        .keyType(KeyType.RANGE)
                        .build()
                )
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName("ID")
                        .attributeType(ScalarAttributeType.N)
                        .build(),
                    AttributeDefinition.builder()
                        .attributeName("No")
                        .attributeType(ScalarAttributeType.S)
                        .build()
                )
                .provisionedThroughput(
                    ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build()
                )
                .build();

            client.createTable(request);
            System.out.println("Table Customer created successfully!");

        } catch (Exception e) {
            System.err.println("Error creating table:");
            System.err.println(e.getMessage());
        }
    }
}