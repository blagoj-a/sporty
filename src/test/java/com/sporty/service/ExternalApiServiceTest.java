package com.sporty.service;

import com.sporty.mapper.EventMessageMapper;
import com.sporty.model.EventMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.MediaType;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@ActiveProfiles({"local", "test"})
class ExternalApiServiceTest {

    private final ClientAndServer mockServer;

    public ExternalApiServiceTest(ClientAndServer mockServer) {
        this.mockServer = mockServer;
    }

    @BeforeEach
    void prepare() {
        mockServer.reset();
    }

    @Test
    void testGetResultsAndPublish() {
        /**

        KafkaPublisherService kafkaPublisherService = mock(KafkaPublisherService.class);
        EventMessageMapper eventMessageMapper = mock(EventMessageMapper.class);

        int localPort = mockServer.getLocalPort();
        ExternalApiService externalApiService = new ExternalApiService("http://localhost:" + localPort,
                kafkaPublisherService, eventMessageMapper);

        String responseBody =
                """
                {"eventId": "12345", "currentScore": "2:0"}
                """;

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/score/12345"))
                .respond(response(responseBody).withContentType(MediaType.APPLICATION_JSON));
        EventMessage mockEventMessage = new EventMessage();
        when(eventMessageMapper.mapToEventMessage(any())).thenReturn(mockEventMessage);
         *
         */

    }
}