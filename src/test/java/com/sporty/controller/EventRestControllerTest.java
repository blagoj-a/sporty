package com.sporty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.model.EventStatusEnum;
import com.sporty.model.EventStatusRequest;
import com.sporty.service.EventStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class EventRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    EventRestController eventRestController;

    @Mock
    private EventStatusService eventStatusService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        mockMvc = MockMvcBuilders.standaloneSetup(eventRestController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testUpdateEventStatus() throws Exception {
        String eventId = "1234";
        EventStatusRequest eventStatusRequest = EventStatusRequest.builder()
                .eventId(eventId)
                .status(EventStatusEnum.LIVE)
                .build();
        String responseMessage = "Event status updated successfully";

        when(eventStatusService.updateStatus(eq(eventStatusRequest))).thenReturn(responseMessage);

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventStatusRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(responseMessage));

        verify(eventStatusService, times(1)).updateStatus(eventStatusRequest);
    }

}