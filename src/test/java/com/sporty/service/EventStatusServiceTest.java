package com.sporty.service;

import com.sporty.model.EventStatusEnum;
import com.sporty.model.EventStatusRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@MockitoSettings
class EventStatusServiceTest {

    @InjectMocks
    EventStatusService eventStatusService;

    @Mock
    EventSchedulerService eventSchedulerService;

    @Test
    void testUpdateStatus_LIVE() throws Exception {
        String eventId = "12345";
        EventStatusRequest eventStatusRequest = EventStatusRequest.builder()
                .eventId(eventId)
                .status(EventStatusEnum.LIVE)
                .build();

        String mockResponse = "Scheduled for receving live results";
        when(eventSchedulerService.scheduleEvent(eventId)).thenReturn(mockResponse);
        String response = eventStatusService.updateStatus(eventStatusRequest);
        assertAll(
                () -> assertThat(response).isEqualTo(mockResponse),
                () -> verify(eventSchedulerService, times(1)).scheduleEvent(eventId)
        );
    }

    @Test
    void testUpdateStatus_NOT_LIVE() throws Exception {
        String eventId = "12345";
        EventStatusRequest eventStatusRequest = EventStatusRequest.builder()
                .eventId(eventId)
                .status(EventStatusEnum.NOT_LIVE)
                .build();

        String mockResponse = "Removed from receiving live results";
        when(eventSchedulerService.cancelEvent(eventId)).thenReturn(mockResponse);
        String response = eventStatusService.updateStatus(eventStatusRequest);
        assertAll(
                () -> assertThat(response).isEqualTo(mockResponse),
                () -> verify(eventSchedulerService, times(1)).cancelEvent(eventId)
        );
    }
}