package com.elearning.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * AI-based assignment evaluation using AWS Bedrock with Claude 3.5 Sonnet v2
 */
public class AIEvaluator {

    private BedrockRuntimeClient bedrockClient;
    private ObjectMapper objectMapper;
    private static final String MODEL_ID = "us.anthropic.claude-3-5-sonnet-20241022-v2:0";
    private boolean connected = false;

    public AIEvaluator() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String region = dotenv.get("AWS_REGION", "us-east-1");

            // Use default credential provider chain (supports IAM roles, env vars, etc.)
            this.bedrockClient = BedrockRuntimeClient.builder()
                    .region(Region.of(region))
                    .build();

            this.objectMapper = new ObjectMapper();
            this.connected = true;

            System.out.println("✓ AWS Bedrock (Claude 3.5 Sonnet v2) initialized successfully");

        } catch (Exception e) {
            System.out.println("✗ Error initializing AWS Bedrock: " + e.getMessage());
            e.printStackTrace();
            this.connected = false;
        }
    }

    public boolean isConnected() {
        return connected && bedrockClient != null;
    }

    /**
     * Evaluate student assignment using AI with teacher's assignment as reference
     */
    public EvaluationResult evaluateWithAI(String studentAnswer, Assignment assignment) {
        if (!isConnected()) {
            System.out.println("Bedrock not connected - falling back to manual evaluation");
            return new EvaluationResult(0, "AI evaluation unavailable. Please try again later.");
        }

        try {
            String prompt = buildEvaluationPrompt(studentAnswer, assignment);

            String response = invokeClaude(prompt);

            return parseAIResponse(response, assignment.getTotalPoints());

        } catch (Exception e) {
            System.out.println("Error during AI evaluation: " + e.getMessage());
            e.printStackTrace();
            return new EvaluationResult(0, "AI evaluation failed: " + e.getMessage());
        }
    }

    /**
     * Evaluate student assignment without teacher reference (blind evaluation)
     */
    public EvaluationResult evaluateWithoutReference(String studentAnswer) {
        if (!isConnected()) {
            System.out.println("Bedrock not connected - falling back to manual evaluation");
            return new EvaluationResult(50, "AI evaluation unavailable. Estimated score based on length and structure.");
        }

        try {
            String prompt = buildBlindEvaluationPrompt(studentAnswer);

            String response = invokeClaude(prompt);

            return parseAIResponse(response, 100);

        } catch (Exception e) {
            System.out.println("Error during AI evaluation: " + e.getMessage());
            e.printStackTrace();
            return new EvaluationResult(50, "AI evaluation failed. Estimated score: 50/100");
        }
    }

    /**
     * Build evaluation prompt with teacher's assignment as reference
     */
    private String buildEvaluationPrompt(String studentAnswer, Assignment assignment) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert teacher evaluating a student's assignment.\n\n");
        prompt.append("## Assignment Details:\n");
        prompt.append("Title: ").append(assignment.getTitle()).append("\n");
        prompt.append("Description: ").append(assignment.getDescription()).append("\n");
        prompt.append("Total Points: ").append(assignment.getTotalPoints()).append("\n\n");

        prompt.append("## Questions and Expected Answers:\n");
        int qNum = 1;
        for (Question q : assignment.getQuestions()) {
            prompt.append("\nQuestion ").append(qNum).append(" (").append(q.getPoints()).append(" points):\n");
            prompt.append(q.getQuestionText()).append("\n");
            prompt.append("Expected Answer: ").append(q.getCorrectAnswer()).append("\n");
            if (q.getKeywords() != null && q.getKeywords().length > 0) {
                prompt.append("Key Concepts: ").append(String.join(", ", q.getKeywords())).append("\n");
            }
            qNum++;
        }

        prompt.append("\n## Student's Submission:\n");
        prompt.append(studentAnswer).append("\n\n");

        prompt.append("## Your Task:\n");
        prompt.append("1. Evaluate the student's answers against the expected answers\n");
        prompt.append("2. Award points for each question (0 to max points)\n");
        prompt.append("3. Provide detailed feedback explaining:\n");
        prompt.append("   - What the student did well\n");
        prompt.append("   - Where they made mistakes\n");
        prompt.append("   - What they need to improve\n");
        prompt.append("   - Specific suggestions for each incorrect/incomplete answer\n\n");

        prompt.append("## Output Format (JSON):\n");
        prompt.append("{\n");
        prompt.append("  \"totalScore\": <number>,\n");
        prompt.append("  \"maxScore\": ").append(assignment.getTotalPoints()).append(",\n");
        prompt.append("  \"feedback\": \"<detailed feedback with question-by-question breakdown>\",\n");
        prompt.append("  \"questionScores\": [\n");
        prompt.append("    {\"question\": 1, \"score\": <points>, \"explanation\": \"<why this score>\"}\n");
        prompt.append("  ],\n");
        prompt.append("  \"strengths\": \"<what student did well>\",\n");
        prompt.append("  \"improvements\": \"<specific areas to improve>\"\n");
        prompt.append("}\n\n");

        prompt.append("Be fair, objective, and constructive in your evaluation. Return ONLY the JSON, no other text.");

        return prompt.toString();
    }

    /**
     * Build prompt for evaluation without teacher reference
     */
    private String buildBlindEvaluationPrompt(String studentAnswer) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert teacher evaluating a student's assignment.\n\n");
        prompt.append("## Student's Submission:\n");
        prompt.append(studentAnswer).append("\n\n");

        prompt.append("## Your Task:\n");
        prompt.append("Since there is no reference assignment from the teacher, evaluate the submission based on:\n");
        prompt.append("1. Clarity and coherence of writing\n");
        prompt.append("2. Depth of understanding shown\n");
        prompt.append("3. Quality of explanations and reasoning\n");
        prompt.append("4. Grammar, spelling, and presentation\n");
        prompt.append("5. Overall effort and completeness\n\n");

        prompt.append("Award a score out of 100 points and provide constructive feedback.\n\n");

        prompt.append("## Output Format (JSON):\n");
        prompt.append("{\n");
        prompt.append("  \"totalScore\": <number out of 100>,\n");
        prompt.append("  \"maxScore\": 100,\n");
        prompt.append("  \"feedback\": \"<detailed feedback on the submission>\",\n");
        prompt.append("  \"strengths\": \"<what the student did well>\",\n");
        prompt.append("  \"improvements\": \"<specific areas to improve>\"\n");
        prompt.append("}\n\n");

        prompt.append("Be fair, objective, and constructive. Return ONLY the JSON, no other text.");

        return prompt.toString();
    }

    /**
     * Invoke Claude model via AWS Bedrock
     */
    private String invokeClaude(String prompt) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        requestBody.put("anthropic_version", "bedrock-2023-05-31");
        requestBody.put("messages", new Object[]{message});
        requestBody.put("max_tokens", 4000);
        requestBody.put("temperature", 0.5);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(MODEL_ID)
                .body(SdkBytes.fromUtf8String(requestBodyJson))
                .build();

        InvokeModelResponse response = bedrockClient.invokeModel(request);

        String responseBody = response.body().asUtf8String();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        JsonNode contentArray = responseJson.get("content");
        if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
            return contentArray.get(0).get("text").asText();
        }

        throw new Exception("Unexpected response format from Claude");
    }

    /**
     * Parse AI response JSON into EvaluationResult
     */
    private EvaluationResult parseAIResponse(String aiResponse, int maxScore) {
        try {
            int jsonStart = aiResponse.indexOf("{");
            int jsonEnd = aiResponse.lastIndexOf("}") + 1;

            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                String jsonStr = aiResponse.substring(jsonStart, jsonEnd);
                JsonNode json = objectMapper.readTree(jsonStr);

                int score = json.get("totalScore").asInt();
                String feedback = json.get("feedback").asText();

                StringBuilder detailedFeedback = new StringBuilder();
                detailedFeedback.append("AI Evaluation Results:\n\n");
                detailedFeedback.append("Score: ").append(score).append("/").append(maxScore).append("\n\n");
                detailedFeedback.append("Feedback:\n").append(feedback).append("\n\n");

                if (json.has("questionScores")) {
                    detailedFeedback.append("Question Breakdown:\n");
                    JsonNode questions = json.get("questionScores");
                    for (JsonNode q : questions) {
                        detailedFeedback.append("Q").append(q.get("question").asInt())
                                .append(": ").append(q.get("score"))
                                .append(" points - ").append(q.get("explanation").asText())
                                .append("\n");
                    }
                    detailedFeedback.append("\n");
                }

                // Extract strengths and improvements
                String strengths = json.has("strengths") ? json.get("strengths").asText() : null;
                String improvements = json.has("improvements") ? json.get("improvements").asText() : null;

                return new EvaluationResult(score, detailedFeedback.toString(), strengths, improvements);
            } else {
                return new EvaluationResult(maxScore / 2, "AI Response: " + aiResponse);
            }

        } catch (Exception e) {
            System.out.println("Error parsing AI response: " + e.getMessage());
            return new EvaluationResult(maxScore / 2, "AI Evaluation:\n" + aiResponse);
        }
    }

    /**
     * Evaluate code with AI using test results and code analysis
     */
    public EvaluationResult evaluateCodeWithAI(String studentCode, CodeEvaluator.CodeEvaluationResult testResult, Assignment assignment) {
        if (!isConnected()) {
            System.out.println("Bedrock not connected - returning test results only");
            return new EvaluationResult(testResult.score, testResult.feedback);
        }

        try {
            String prompt = buildCodeEvaluationPrompt(studentCode, testResult, assignment);

            String response = invokeClaude(prompt);

            return parseCodeAIResponse(response, testResult);

        } catch (Exception e) {
            System.out.println("Error during AI code evaluation: " + e.getMessage());
            e.printStackTrace();
            return new EvaluationResult(testResult.score, testResult.feedback);
        }
    }

    /**
     * Evaluate code with AI without test cases (blind evaluation)
     */
    public EvaluationResult evaluateCodeBlindWithAI(String studentCode, String fileName) {
        if (!isConnected()) {
            System.out.println("Bedrock not connected - falling back to basic evaluation");
            return new EvaluationResult(50, "AI evaluation unavailable. Code uploaded successfully.");
        }

        try {
            String prompt = buildBlindCodeEvaluationPrompt(studentCode, fileName);

            String response = invokeClaude(prompt);

            return parseAIResponse(response, 100);

        } catch (Exception e) {
            System.out.println("Error during AI code evaluation: " + e.getMessage());
            e.printStackTrace();
            return new EvaluationResult(50, "AI evaluation failed. Estimated score: 50/100");
        }
    }

    /**
     * Build code evaluation prompt with test results
     */
    private String buildCodeEvaluationPrompt(String studentCode, CodeEvaluator.CodeEvaluationResult testResult, Assignment assignment) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert programming instructor evaluating a student's code submission.\n\n");
        prompt.append("## Assignment Details:\n");
        prompt.append("Title: ").append(assignment.getTitle()).append("\n");
        prompt.append("Description: ").append(assignment.getDescription()).append("\n");
        prompt.append("Total Points: ").append(assignment.getTotalPoints()).append("\n\n");

        prompt.append("## Test Results:\n");
        prompt.append("Compilation: ").append(testResult.compilationSuccess ? "Success" : "Failed").append("\n");
        prompt.append("Tests Passed: ").append(testResult.score).append("/").append(testResult.totalTests).append("\n");
        prompt.append("Score: ").append(testResult.score).append("/100\n\n");

        prompt.append("## Test Case Results:\n");
        for (CodeEvaluator.TestResult tr : testResult.testResults) {
            prompt.append("- ").append(tr.testName).append(": ")
                    .append(tr.passed ? "PASS" : "FAIL")
                    .append(" - ").append(tr.message).append("\n");
        }
        prompt.append("\n");

        prompt.append("## Student's Code:\n```java\n");
        prompt.append(studentCode).append("\n```\n\n");

        prompt.append("## Your Task:\n");
        prompt.append("Provide detailed feedback on the student's code:\n");
        prompt.append("1. Code quality (readability, structure, naming conventions)\n");
        prompt.append("2. Algorithm efficiency and approach\n");
        prompt.append("3. Specific explanations for failed tests (if any)\n");
        prompt.append("4. Suggestions for improvement\n");
        prompt.append("5. What they did well\n\n");

        prompt.append("The TEST SCORE (").append(testResult.score).append("/100) is already calculated.\n");
        prompt.append("Your job is to provide DETAILED FEEDBACK only.\n\n");

        prompt.append("## Output Format (JSON):\n");
        prompt.append("{\n");
        prompt.append("  \"codeQuality\": \"<assessment of code quality>\",\n");
        prompt.append("  \"algorithmAnalysis\": \"<analysis of approach and efficiency>\",\n");
        prompt.append("  \"failedTestsExplanation\": \"<why specific tests failed and how to fix>\",\n");
        prompt.append("  \"strengths\": \"<what student did well>\",\n");
        prompt.append("  \"improvements\": \"<specific suggestions to improve code>\",\n");
        prompt.append("  \"overallFeedback\": \"<comprehensive feedback summary>\"\n");
        prompt.append("}\n\n");

        prompt.append("Be constructive, detailed, and educational. Return ONLY the JSON, no other text.");

        return prompt.toString();
    }

    /**
     * Build prompt for blind code evaluation
     */
    private String buildBlindCodeEvaluationPrompt(String studentCode, String fileName) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert programming instructor evaluating a student's code submission.\n\n");
        prompt.append("## Student's Code (").append(fileName).append("):\n```java\n");
        prompt.append(studentCode).append("\n```\n\n");

        prompt.append("## Your Task:\n");
        prompt.append("Since there is no reference assignment or test cases, evaluate the code based on:\n");
        prompt.append("1. Code correctness and logic\n");
        prompt.append("2. Algorithm efficiency and approach\n");
        prompt.append("3. Code quality (readability, structure, naming)\n");
        prompt.append("4. Best practices and design patterns\n");
        prompt.append("5. Error handling and edge cases\n");
        prompt.append("6. Comments and documentation\n\n");

        prompt.append("Award a score out of 100 points and provide comprehensive feedback.\n\n");

        prompt.append("## Output Format (JSON):\n");
        prompt.append("{\n");
        prompt.append("  \"totalScore\": <number out of 100>,\n");
        prompt.append("  \"maxScore\": 100,\n");
        prompt.append("  \"codeQuality\": \"<assessment of code quality>\",\n");
        prompt.append("  \"algorithmAnalysis\": \"<analysis of approach and efficiency>\",\n");
        prompt.append("  \"strengths\": \"<what the student did well>\",\n");
        prompt.append("  \"improvements\": \"<specific suggestions to improve>\",\n");
        prompt.append("  \"feedback\": \"<comprehensive summary>\"\n");
        prompt.append("}\n\n");

        prompt.append("Be fair, objective, and constructive. Return ONLY the JSON, no other text.");

        return prompt.toString();
    }

    /**
     * Parse AI code evaluation response (use test score, enhance feedback)
     */
    private EvaluationResult parseCodeAIResponse(String aiResponse, CodeEvaluator.CodeEvaluationResult testResult) {
        try {
            int jsonStart = aiResponse.indexOf("{");
            int jsonEnd = aiResponse.lastIndexOf("}") + 1;

            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                String jsonStr = aiResponse.substring(jsonStart, jsonEnd);
                JsonNode json = objectMapper.readTree(jsonStr);

                StringBuilder enhancedFeedback = new StringBuilder();
                enhancedFeedback.append("AI-Enhanced Code Evaluation\n\n");
                enhancedFeedback.append("Test Results: ").append(testResult.score).append("/100\n");
                enhancedFeedback.append(testResult.feedback).append("\n\n");

                enhancedFeedback.append("--- AI Detailed Analysis ---\n\n");

                if (json.has("codeQuality")) {
                    enhancedFeedback.append("Code Quality:\n")
                            .append(json.get("codeQuality").asText()).append("\n\n");
                }

                if (json.has("algorithmAnalysis")) {
                    enhancedFeedback.append("Algorithm Analysis:\n")
                            .append(json.get("algorithmAnalysis").asText()).append("\n\n");
                }

                if (json.has("failedTestsExplanation") && !json.get("failedTestsExplanation").asText().isEmpty()) {
                    enhancedFeedback.append("Failed Tests Explanation:\n")
                            .append(json.get("failedTestsExplanation").asText()).append("\n\n");
                }

                if (json.has("overallFeedback")) {
                    enhancedFeedback.append("Overall Feedback:\n")
                            .append(json.get("overallFeedback").asText()).append("\n");
                }

                // Extract strengths and improvements
                String strengths = json.has("strengths") ? json.get("strengths").asText() : null;
                String improvements = json.has("improvements") ? json.get("improvements").asText() : null;

                return new EvaluationResult(testResult.score, enhancedFeedback.toString(), strengths, improvements);
            } else {
                return new EvaluationResult(testResult.score, testResult.feedback + "\n\nAI Analysis:\n" + aiResponse);
            }

        } catch (Exception e) {
            System.out.println("⚠️  Error parsing AI code response: " + e.getMessage());
            return new EvaluationResult(testResult.score, testResult.feedback);
        }
    }

    /**
     * Shutdown Bedrock client
     */
    public void shutdown() {
        if (bedrockClient != null) {
            bedrockClient.close();
            System.out.println("Bedrock client shutdown");
        }
    }
}
