package com.elearning.bdd;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

public abstract class BDDTestBase {

    protected static final String TEST_WORKSPACE = "/tmp/bdd_test_workspace";
    protected File testWorkspace;

    @BeforeEach
    void setupTestEnvironment() {
        testWorkspace = new File(TEST_WORKSPACE);
        testWorkspace.mkdirs();
        System.out.println("\nSetting up test environment: " + TEST_WORKSPACE);
    }

    @AfterEach
    void cleanupTestEnvironment() {
        deleteDirectory(testWorkspace);
        System.out.println("Cleaned up test environment\n");
    }

    
    protected File createTestFile(String fileName, String content) throws IOException {
        File file = new File(testWorkspace, fileName);
        Files.write(file.toPath(), content.getBytes());
        return file;
    }


    protected void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
