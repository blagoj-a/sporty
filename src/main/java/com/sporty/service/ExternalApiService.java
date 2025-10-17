package com.sporty.service;

import com.sporty.mapper.EventMessageMapper;
import com.sporty.model.ExternalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class ExternalApiService {

    private final WebClient webClient;
    private final KafkaPublisherService kafkaPublisherService;
    private final EventMessageMapper eventMessageMapper;

    public ExternalApiService(@Value("${app.external-api.url}") String externalApiUrl, KafkaPublisherService kafkaPublisherService, EventMessageMapper eventMessageMapper) {
        this.webClient = WebClient.builder().baseUrl(externalApiUrl).build();
        this.kafkaPublisherService = kafkaPublisherService;
        this.eventMessageMapper = eventMessageMapper;
    }

    public void getResultsAndPublish(String eventId) {
        webClient.get()
                .uri("/score/{id}", eventId)
                .retrieve()
                .bodyToMono(ExternalApiResponse.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("Error polling mock API for event {}: {}", eventId, e.getMessage());
                    return Mono.empty();
                })
                .map(externalApiResponse -> eventMessageMapper.mapToEventMessage(externalApiResponse))
                .doOnSuccess(message -> kafkaPublisherService.publishToKafka(message, 1))
                .subscribe();
    }
}
