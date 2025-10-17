package com.sporty.mapper;

import com.sporty.model.EventMessage;
import com.sporty.model.ExternalApiResponse;
import org.springframework.stereotype.Component;

@Component
public class EventMessageMapper {

    public EventMessage mapToEventMessage(ExternalApiResponse externalApiResponse) {
        return EventMessage.builder()
                .eventId(externalApiResponse.getEventId())
                .score(externalApiResponse.getCurrentScore())
                .build();
    }
}
