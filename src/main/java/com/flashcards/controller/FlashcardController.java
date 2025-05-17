package com.flashcards.controller;

import com.flashcards.dto.FlashcardRequest;
import com.flashcards.dto.FlashcardResponse;
import com.flashcards.service.FlashcardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FlashcardController {

    private final FlashcardService service;

    public FlashcardController(FlashcardService service) {
        this.service = service;
    }

    @PostMapping("/flashcard")
    public Map<String, String> addFlashcard(@RequestBody FlashcardRequest request) {
        return service.addFlashcard(request);
    }

    @GetMapping("/get-subject")
    public List<FlashcardResponse> getMixedFlashcards(
            @RequestParam String student_id,
            @RequestParam(defaultValue = "5") int limit) {
        return service.getMixedFlashcards(student_id, limit);
    }
}
