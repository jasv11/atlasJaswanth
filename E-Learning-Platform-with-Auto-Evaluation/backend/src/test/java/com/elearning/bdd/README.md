# BDD Tests for E-Learning Platform

## Overview

This folder contains Behavior-Driven Development (BDD) tests for the E-Learning Platform.

**Important:** These are TEST ONLY files. They do NOT affect production data or the running application.

---

## File Structure

```
bdd/
├── BDDTestBase.java                  - Base class for all tests
├── Day4_CodeEvaluationTest.java      - Test Case Runner tests
├── Day7_HashMapPerformanceTest.java  - HashMap performance tests
├── IntegrationWorkflowTest.java      - End-to-end workflow tests
└── README.md                         - This file
```

---

## How to Run Tests

### Run ALL BDD Tests
```bash
cd backend
mvn test -Dtest="com.elearning.bdd.*"
```

### Run Individual Test Files

**Day 4 - Code Evaluation Tests (5 scenarios)**
```bash
mvn test -Dtest=Day4_CodeEvaluationTest
```

**Day 7 - HashMap Performance Tests (4 scenarios)**
```bash
mvn test -Dtest=Day7_HashMapPerformanceTest
```

**Integration Workflow Tests (2 scenarios)**
```bash
mvn test -Dtest=IntegrationWorkflowTest
```

### Run Specific Test Scenario

```bash
# TwoSum test only
mvn test -Dtest=Day4_CodeEvaluationTest#twoSumProblem_CorrectSolution_AllTestsPass

# Factorial test only
mvn test -Dtest=Day4_CodeEvaluationTest#factorialProblem_CorrectSolution_AllTestsPass

# HashMap O(1) performance test only
mvn test -Dtest=Day7_HashMapPerformanceTest#hashMapPerformance_O1_Lookup
```

---
