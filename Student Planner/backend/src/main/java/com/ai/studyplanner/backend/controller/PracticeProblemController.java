package com.ai.studyplanner.backend.controller;

import com.ai.studyplanner.backend.dto.PracticeProblem;
import com.ai.studyplanner.backend.dto.PracticeProblemRequest;
import com.ai.studyplanner.backend.dto.QuizAttempt;
import com.ai.studyplanner.backend.dto.QuizAttemptRequest;
import com.ai.studyplanner.backend.service.PracticeProblemService;
import com.ai.studyplanner.backend.store.QuizAttemptStore;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/practice")
@CrossOrigin(origins = "*")
public class PracticeProblemController {

    private final PracticeProblemService service;
    private final QuizAttemptStore quizAttemptStore;

    public PracticeProblemController(
            PracticeProblemService service,
            QuizAttemptStore quizAttemptStore
    ) {
        this.service = service;
        this.quizAttemptStore = quizAttemptStore;
    }

    @PostMapping("/generate")
    public List<PracticeProblem> generate(
            @RequestBody PracticeProblemRequest request
    ) {
        return service.generateFromPdf(request.getPdfId());
    }

    @PostMapping("/submit")
    public QuizAttempt submitAttempt(
            @RequestBody QuizAttemptRequest request
    ) {
        QuizAttempt attempt = new QuizAttempt(
                Instant.now(),
                request.getTotalQuestions(),
                request.getCorrectAnswers(),
                request.getScorePercentage()
        );

        quizAttemptStore.addAttempt(request.getPdfId(), attempt);
        return attempt;
    }

    @GetMapping("/attempts/{pdfId}")
    public List<QuizAttempt> getAttempts(@PathVariable String pdfId) {
        return quizAttemptStore.getAttempts(pdfId);
    }
}
