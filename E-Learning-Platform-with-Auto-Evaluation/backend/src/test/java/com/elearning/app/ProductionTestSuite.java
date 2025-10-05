package com.elearning.app;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({
    AWSConnectionTest.class,
    DynamoDBIntegrationTest.class,
    ScoreManagerIntegrationTest.class,
    CodeEvaluatorIntegrationTest.class,
    AssignmentEvaluatorIntegrationTest.class
})
public class ProductionTestSuite {
    // Test suite runner - no implementation needed
}
