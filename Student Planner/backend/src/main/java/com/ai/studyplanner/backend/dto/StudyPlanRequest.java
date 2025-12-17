package com.ai.studyplanner.backend.dto;

import java.util.List;

public class StudyPlanRequest {
    private List<String> topics;

    public StudyPlanRequest() {}

    public StudyPlanRequest(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
