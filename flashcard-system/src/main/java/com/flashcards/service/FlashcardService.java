package com.flashcards.service;

import com.flashcards.dto.FlashcardRequest;
import com.flashcards.dto.FlashcardResponse;
import com.flashcards.model.Flashcard;
import com.flashcards.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    private final FlashcardRepository repository;

    public FlashcardService(FlashcardRepository repository) {
        this.repository = repository;
    }

    public Map<String, String> addFlashcard(FlashcardRequest request) {
        String subject = detectSubject(request.question() + " " + request.answer());

        Flashcard card = Flashcard.builder()
                .studentId(request.studentId())
                .question(request.question())
                .answer(request.answer())
                .subject(subject)
                .build();

        repository.save(card);

        return Map.of("message", "Flashcard added successfully", "subject", subject);
    }

    public List<FlashcardResponse> getMixedFlashcards(String studentId, int limit) {
        List<Flashcard> all = repository.findByStudentId(studentId);

        Map<String, List<Flashcard>> bySubject = all.stream()
                .collect(Collectors.groupingBy(Flashcard::getSubject));

        List<FlashcardResponse> result = new ArrayList<>();

        while (result.size() < limit && !bySubject.isEmpty()) {
            List<String> subjects = new ArrayList<>(bySubject.keySet());
            Collections.shuffle(subjects);

            for (String subject : subjects) {
                List<Flashcard> subjectCards = bySubject.get(subject);
                if (subjectCards.isEmpty()) {
                    bySubject.remove(subject);
                    continue;
                }

                Flashcard card = subjectCards.remove(0);
                result.add(new FlashcardResponse(card.getQuestion(), card.getAnswer(), card.getSubject()));

                if (result.size() == limit) break;
            }
        }
        return result;
    }

    private String detectSubject(String text) {
        text = text.toLowerCase();

        Map<String, List<String>> subjectKeywords = Map.of(
                "Physics", List.of("force", "acceleration", "velocity", "newton", "gravity", "motion"),
                "Biology", List.of("photosynthesis", "cell", "organism", "plant", "enzyme", "mitochondria"),
                "Chemistry", List.of("atom", "molecule", "reaction", "bond", "acid", "base"),
                "Mathematics", List.of("algebra", "integral", "derivative", "equation", "geometry", "calculus"),
                "History", List.of("war", "empire", "revolution", "ancient", "medieval", "king"),
                "Geography", List.of("continent", "ocean", "river", "mountain", "climate", "latitude")
        );

        return subjectKeywords.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(text::contains))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("General");
    }
}
