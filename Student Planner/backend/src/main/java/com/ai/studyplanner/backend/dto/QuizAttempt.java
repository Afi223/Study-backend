package com.ai.studyplanner.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttempt {
    private Instant timestamp;
    private int totalQuestions;
    private int correctAnswers;
    private int scorePercentage;
}
