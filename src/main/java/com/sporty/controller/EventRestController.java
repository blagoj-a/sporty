package com.sporty.controller;

import com.sporty.model.EventStatusRequest;
import com.sporty.service.EventStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventRestController {
    private final EventStatusService eventStatusService;

    @PostMapping("/status")
    public String updateEventStatus(@RequestBody EventStatusRequest update) throws Exception {
        return eventStatusService.updateStatus(update);
    }
}
