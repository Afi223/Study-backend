package com.ai.studyplanner.backend.controller;

import com.ai.studyplanner.backend.dto.TopicsExtractRequest;
import com.ai.studyplanner.backend.dto.TopicsExtractResponse;
import com.ai.studyplanner.backend.service.TopicExtractionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "*") // later restrict to Vercel domain
public class TopicsController {

    private final TopicExtractionService topicService;

    public TopicsController(TopicExtractionService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/extract")
    public TopicsExtractResponse extractTopics(
            @Valid @RequestBody TopicsExtractRequest request
    ) {
        return new TopicsExtractResponse(
                topicService.extractTopics(request.getSyllabusText())
        );
    }
}
