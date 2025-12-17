package com.ai.studyplanner.backend.dto;

public record StudyPlanItem(
        int week,
        String topic,
        int hours,
        String difficulty
) {}
