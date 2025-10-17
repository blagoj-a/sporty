package com.sporty.service;

import com.sporty.model.EventStatusEnum;
import com.sporty.model.EventStatusRequest;
import jakarta.annotation.PreDestroy;
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
public class EventStatusService {

    private final EventSchedulerService eventSchedulerService;

    public String updateStatus(EventStatusRequest event) throws Exception {

        if (EventStatusEnum.LIVE == event.getStatus()) {
            return eventSchedulerService.scheduleEvent(event.getEventId());
        } else if (EventStatusEnum.NOT_LIVE == event.getStatus()) {
            return eventSchedulerService.cancelEvent(event.getEventId());
        } else {
            log.warn("Invalid status received for event {}: {}", event.getEventId(), event.getStatus());
            throw new Exception("Invalid event status: " + event.getStatus());
        }
    }
}
