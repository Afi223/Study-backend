package com.ai.studyplanner.backend.service;

import com.ai.studyplanner.backend.dto.StudyPlanItem;
import com.ai.studyplanner.backend.dto.StudyPlanResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyPlanService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public StudyPlanService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public StudyPlanResponse generatePlan(List<String> topics) {
        try {
            String systemPrompt = """
                You are an assistant that creates structured study plans.
                You MUST respond with valid JSON only.
                Do NOT include explanations or markdown.
                """;

            String userPrompt = """
                Given the following topics, generate a weekly study plan.

                Rules:
                - Each topic gets its own week
                - hours is an integer between 2 and 6
                - difficulty must be one of: Easy, Medium, Hard

                Topics:
                %s

                Return JSON in this exact format:
                {
                  "plan": [
                    { "week": 1, "topic": "Arrays", "hours": 4, "difficulty": "Easy" }
                  ]
                }
                """.formatted(topics);

            String aiResponse = openAiClient.chat(systemPrompt, userPrompt);

            JsonNode root = mapper.readTree(aiResponse);
            JsonNode planNode = root.get("plan");

            List<StudyPlanItem> plan = new ArrayList<>();

            for (JsonNode node : planNode) {
                plan.add(new StudyPlanItem(
                        node.get("week").asInt(),
                        node.get("topic").asText(),
                        node.get("hours").asInt(),
                        node.get("difficulty").asText()
                ));
            }

            return new StudyPlanResponse(plan);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate study plan");
        }
    }
}
