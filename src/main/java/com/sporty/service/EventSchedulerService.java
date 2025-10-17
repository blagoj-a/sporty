package com.sporty.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventSchedulerService {
    private final ThreadPoolTaskScheduler taskScheduler;
    private final ExternalApiService externalApiService;

    public final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    public static final Duration POLLING_INTERVAL = Duration.ofSeconds(10);


    public String scheduleEvent(String eventId) {
        if (scheduledTasks.containsKey(eventId)) {
            log.info("Event {} is already live. Polling is active.", eventId);
            return "Event " + eventId + " is already scheduled and active.";
        }

        Runnable task = () -> externalApiService.getResultsAndPublish(eventId);

        // Schedule the task to run repeatedly every 10 seconds
        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(task, POLLING_INTERVAL);

        scheduledTasks.put(eventId, future);
        log.info("Scheduled new polling task for event {}.", eventId);
        return "Scheduled polling for event: " + eventId;
    }

    public String cancelEvent(String eventId) {
        ScheduledFuture<?> future = scheduledTasks.remove(eventId);
        if (future != null) {

            future.cancel(true);
            log.info("Cancelled polling task for event {}.", eventId);
            return "Cancelled polling for event: " + eventId;
        } else {
            log.info("Attempted to cancel non-scheduled event: {}", eventId);
            return "Event " + eventId + " was not currently scheduled for polling.";
        }
    }
}
