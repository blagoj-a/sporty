package com.sporty.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
class EventSchedulerServiceTest {

    @InjectMocks
    EventSchedulerService eventSchedulerService;

    @Mock
    ThreadPoolTaskScheduler taskScheduler;

    @Mock
    ExternalApiService externalApiService;

    @Mock
    ScheduledFuture<?> scheduledFuture;

    @Test
    void testScheduleEvent_Success() {
        String eventId = "event1";
        when(taskScheduler.scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10))))
                .thenAnswer(invocation -> {
                    Runnable task = invocation.getArgument(0);
                    task.run(); // Execute the task to verify it calls the correct method
                    return scheduledFuture;
                });

        String result = eventSchedulerService.scheduleEvent(eventId);
        assertAll(
                () -> assertEquals("Scheduled polling for event: " + eventId, result),
                () -> assertTrue(eventSchedulerService.scheduledTasks.containsKey(eventId)),
                () -> assertEquals(scheduledFuture, eventSchedulerService.scheduledTasks.get(eventId)),
                () -> verify(taskScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10))),
                () -> verify(externalApiService, times(1)).getResultsAndPublish(eventId)
        );

    }

    @Test
    void testScheduleEvent_AlreadyScheduled() {
        String eventId = "event2";
        eventSchedulerService.scheduledTasks.put(eventId, scheduledFuture);

        String result = eventSchedulerService.scheduleEvent(eventId);

        assertAll(
                () -> assertEquals("Event " + eventId + " is already scheduled and active.", result),
                () -> verify(taskScheduler, never()).scheduleAtFixedRate(any(Runnable.class), any(Duration.class))
        );
    }

    @Test
    void testScheduleEvent_TaskExecution() {
        String eventId = "event3";
        when(taskScheduler.scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10))))
                .thenAnswer(invocation -> {
                    Runnable task = invocation.getArgument(0);
                    task.run(); // Execute the task to verify it calls the correct method
                    return scheduledFuture;
                });

        eventSchedulerService.scheduleEvent(eventId);

        verify(externalApiService, times(1)).getResultsAndPublish(eventId);
    }

    @Test
    void testCancelEvent_Success() {
        String eventId = "event4";
        eventSchedulerService.scheduledTasks.put(eventId, scheduledFuture);
        when(scheduledFuture.cancel(true)).thenReturn(true);

        String result = eventSchedulerService.cancelEvent(eventId);
        assertAll(
                () -> assertEquals("Cancelled polling for event: " + eventId, result),
                () -> assertFalse(eventSchedulerService.scheduledTasks.containsKey(eventId)),
                () -> verify(scheduledFuture, times(1)).cancel(true)
        );
    }

    @Test
    void testCancelEvent_NotScheduled() {
        String eventId = "event5";

        String result = eventSchedulerService.cancelEvent(eventId);

        assertAll(
                () -> assertEquals("Event " + eventId + " was not currently scheduled for polling.", result),
                () -> verify(scheduledFuture, never()).cancel(true)
        );
    }

    @Test
    void testCancelEvent_CancelReturnsFalse() {
        String eventId = "event6";
        eventSchedulerService.scheduledTasks.put(eventId, scheduledFuture);
        when(scheduledFuture.cancel(true)).thenReturn(false);

        String result = eventSchedulerService.cancelEvent(eventId);

        assertAll(
                () -> assertEquals("Cancelled polling for event: " + eventId, result),
                () -> assertFalse(eventSchedulerService.scheduledTasks.containsKey(eventId)),
                () -> verify(scheduledFuture, times(1)).cancel(true)
        );
    }

}