package com.ai.studyplanner.backend.service;

import com.ai.studyplanner.backend.dto.PracticeProblem;
import com.ai.studyplanner.backend.store.PdfTextStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracticeProblemService {

    private final OpenAiClient openAiClient;
    private final PdfTextStore pdfTextStore;
    private final ObjectMapper mapper = new ObjectMapper();

    public PracticeProblemService(
            OpenAiClient openAiClient,
            PdfTextStore pdfTextStore
    ) {
        this.openAiClient = openAiClient;
        this.pdfTextStore = pdfTextStore;
    }

    public List<PracticeProblem> generateFromPdf(String pdfId) {

        String pdfText = pdfTextStore.get(pdfId);
        if (pdfText == null || pdfText.isBlank()) {
            throw new IllegalArgumentException("No text found for PDF: " + pdfId);
        }

        String trimmedText = pdfText.length() > 8000
                ? pdfText.substring(0, 8000)
                : pdfText;

        String systemPrompt = """
You are an expert computer science tutor.

Generate EXACTLY 5 multiple-choice practice questions
based ONLY on the content below.

Rules:
- Do NOT reference topics not explicitly mentioned
- Each question must have exactly 4 options
- correctAnswer must be an index (0–3)
- Return RAW JSON ONLY (no markdown, no backticks)

JSON format:
[
  {
    "question": "string",
    "options": ["A", "B", "C", "D"],
    "correctAnswer": 0,
    "explanation": "string"
  }
]
""";

        try {
            String rawResponse = openAiClient.chat(systemPrompt, trimmedText);

            String cleaned = rawResponse
                    .replaceAll("(?s)```json", "")
                    .replaceAll("```", "")
                    .trim();

            List<PracticeProblem> problems = mapper.readValue(
                    cleaned,
                    new TypeReference<List<PracticeProblem>>() {}
            );

            if (problems.size() != 5) {
                throw new RuntimeException(
                        "Expected 5 questions, got " + problems.size()
                );
            }

            return problems;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to generate practice problems. Raw output was invalid.",
                    e
            );
        }
    }
}
