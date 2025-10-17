package com.sporty.controller;

import com.sporty.model.ExternalApiResponse;
import com.sporty.model.ExternalApiScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/mock-api")
@Slf4j
public class ЕxternalApiMockRestController {
    private final Random random = new Random();
    private final ConcurrentHashMap<String, ExternalApiScore> scores = new ConcurrentHashMap<>();

    @GetMapping("/score/{eventId}")
    public ResponseEntity<ExternalApiResponse> getCurrentScore(@PathVariable String eventId) {

        ExternalApiScore mockScore = scores.computeIfAbsent(eventId, k -> new ExternalApiScore());

        if (random.nextBoolean()) {
            mockScore.addHomeScore(random.nextInt(2));
        } else {
            mockScore.addAwayScore(random.nextInt(2));
        }

        log.info("Mock API responding for eventId={}, score={}", eventId, mockScore);

        return ResponseEntity.ok(new ExternalApiResponse(eventId, mockScore.toString()));
    }
}
