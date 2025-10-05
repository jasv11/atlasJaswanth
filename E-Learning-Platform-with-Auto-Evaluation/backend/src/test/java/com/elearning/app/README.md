# Production Integration Tests

## Overview

Comprehensive integration tests to verify E-Learning Platform is ready for production deployment to AWS EC2.

---

## Test Files

| Test File | Purpose | Tests Count |
|-----------|---------|-------------|
| **AWSConnectionTest.java** | AWS connectivity verification | 3 |
| **DynamoDBIntegrationTest.java** | Database operations | 6 |
| **ScoreManagerIntegrationTest.java** | Score management | 6 |
| **CodeEvaluatorIntegrationTest.java** | Code evaluation | 5 |
| **AssignmentEvaluatorIntegrationTest.java** | Text assignment evaluation | 6 |
| **ProductionTestSuite.java** | Run all tests together | - |

**Total:** 26 integration tests

---

## Quick Start

### Run All Production Tests
```bash
cd backend
mvn clean test -Dtest="com.elearning.app.*IntegrationTest"
```

### Run Test Suite
```bash
mvn test -Dtest=ProductionTestSuite
```

### Run Individual Test
```bash
mvn test -Dtest=CodeEvaluatorIntegrationTest
```

---

## What Each Test Verifies

### 1. AWS Connection Test
- AWS credentials configured
- DynamoDB accessible
- S3 accessible
- Bedrock AI accessible

### 2. DynamoDB Integration Test
- Database connection works
- Score save operations succeed
- Score retrieval works
- Query by student ID functions
- Get all scores works
- Error handling proper

### 3. Score Manager Integration Test
- Score save and retrieve
- Filter by student ID
- Cache performance (< 10ms)
- Null handling
- Cache statistics
- JSON serialization

### 4. Code Evaluator Integration Test
- Java code compilation
- Test case execution
- Compilation error handling
- Partial credit system
- File extraction (TXT, ZIP)

### 5. Assignment Evaluator Integration Test
- Text file extraction
- Answer key evaluation
- Keyword matching
- Empty file handling
- Assignment JSON serialization
- Question JSON serialization

---

## Pre-Deployment Requirements

Before deploying to EC2, ALL tests must PASS:

```bash
mvn clean test -Dtest=ProductionTestSuite
```

Expected output:
```
Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Environment Variables Required

```bash
# AWS Credentials
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=us-east-1

# Optional
DYNAMODB_ENDPOINT=http://localhost:8000  # For local testing
```

---

## CI/CD Integration

Add to GitHub Actions workflow:

```yaml
- name: Run Production Tests
  env:
    AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
    AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
    AWS_REGION: us-east-1
  run: |
    cd backend
    mvn clean test -Dtest=ProductionTestSuite
```

---