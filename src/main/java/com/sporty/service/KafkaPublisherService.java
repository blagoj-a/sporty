package com.sporty.service;

import com.sporty.model.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.LeaderNotAvailableException;
import org.apache.kafka.common.errors.NetworkException;
import org.apache.kafka.common.errors.NotEnoughReplicasException;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisherService {

    private final KafkaTemplate<String, EventMessage> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    private static final Set<Class<? extends Throwable>> TRANSIENT_ERRORS = Set.of(
            TimeoutException.class,
            NotEnoughReplicasException.class,
            NetworkException.class,
            LeaderNotAvailableException.class
    );

    public void publishToKafka(EventMessage message, int attempt) {

        kafkaTemplate.send(kafkaTopic, message.getEventId(), message)
               .whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info(
                        "Message sent successfully for event id: {} attempt={} topic={} partition={} offset={}",
                        message.getEventId(), attempt, metadata.topic(), metadata.partition(), metadata.offset()
                );
            } else {
                if (shouldRetry(ex) && attempt < MAX_RETRIES) {
                    log.error("failure sending in attempt={}/{}: {}. Retrying in {} ms...",
                            attempt, MAX_RETRIES, ex.getMessage(), RETRY_DELAY_MS
                    );
                    CompletableFuture
                            .delayedExecutor(RETRY_DELAY_MS, TimeUnit.MILLISECONDS)
                            .execute(() -> publishToKafka(message, attempt + 1));
                } else {
                    log.error("Permanent failure or max retries reached attempt={}: {}", attempt, ex.getMessage());
                }
            }
        });
    }

    private boolean shouldRetry(Throwable ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        return TRANSIENT_ERRORS.stream().anyMatch(clazz -> clazz.isAssignableFrom(cause.getClass()));
    }

}
