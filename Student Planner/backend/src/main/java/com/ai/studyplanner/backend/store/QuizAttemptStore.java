package com.ai.studyplanner.backend.store;

import com.ai.studyplanner.backend.dto.QuizAttempt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuizAttemptStore {

    private final Map<String, List<QuizAttempt>> attemptsByPdf = new HashMap<>();

    public void addAttempt(String pdfId, QuizAttempt attempt) {
        attemptsByPdf
                .computeIfAbsent(pdfId, k -> new ArrayList<>())
                .add(attempt);
    }

    public List<QuizAttempt> getAttempts(String pdfId) {
        return attemptsByPdf.getOrDefault(pdfId, List.of());
    }
}
