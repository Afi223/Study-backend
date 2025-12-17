package com.ai.studyplanner.backend.controller;

import com.ai.studyplanner.backend.service.PdfService;
import com.ai.studyplanner.backend.store.PdfTextStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class PdfController {

    private final PdfService pdfService;
    private final PdfTextStore pdfTextStore;

    public PdfController(PdfService pdfService, PdfTextStore pdfTextStore) {
        this.pdfService = pdfService;
        this.pdfTextStore = pdfTextStore;
    }

    @PostMapping("/upload")
    public Map<String, String> uploadPdf(@RequestParam("file") MultipartFile file)
            throws IOException {

        String extractedText = pdfService.extractText(file);
        String pdfId = pdfTextStore.save(extractedText);

        return Map.of("pdfId", pdfId);
    }
}
