package com.ai.studyplanner.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeProblem {

    private String question;
    private List<String> options;
    private int correctAnswer;
    private String explanation;
}
