package com.ai.studyplanner.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopicExtractionService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TopicExtractionService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public List<String> extractTopics(String syllabusText) {

        String systemPrompt = """
        You are an academic assistant.

        Extract the high-level study topics from the syllabus.

        Return ONLY valid JSON in the following format:
        {
          "topics": ["Topic 1", "Topic 2", "Topic 3"]
        }

        Rules:
        - No explanations
        - No markdown
        - JSON only
        """;

        try {
            String aiResponse = openAiClient.chat(systemPrompt, syllabusText);

            JsonNode root = objectMapper.readTree(aiResponse);
            JsonNode topicsNode = root.get("topics");

            List<String> topics = new ArrayList<>();
            if (topicsNode != null && topicsNode.isArray()) {
                for (JsonNode topic : topicsNode) {
                    topics.add(topic.asText());
                }
            }

            return topics;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
