package com.ai.studyplanner.backend.dto;

import lombok.Data;

@Data
public class QuizAttemptRequest {
    private String pdfId;
    private int totalQuestions;
    private int correctAnswers;
    private int scorePercentage;
}
