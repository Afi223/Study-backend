package com.ai.studyplanner.backend.controller;

import com.ai.studyplanner.backend.dto.StudyPlanRequest;
import com.ai.studyplanner.backend.dto.StudyPlanResponse;
import com.ai.studyplanner.backend.service.StudyPlanService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@CrossOrigin(origins = "*")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @PostMapping("/generate")
    public StudyPlanResponse generatePlan(@RequestBody StudyPlanRequest request) {
        return studyPlanService.generatePlan(request.getTopics());
    }
}
