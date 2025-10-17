package com.sporty.model;

import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventMessage {
    private String eventId;
    private String score;


}
