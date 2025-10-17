package com.sporty.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class EventStatusRequest {

    @NotBlank(message = "eventId is required")
    private String eventId;

    @NotNull(message = "status is required")
    private EventStatusEnum status;
}
