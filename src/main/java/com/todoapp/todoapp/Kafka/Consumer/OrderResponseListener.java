package com.todoapp.todoapp.Kafka.Consumer;

import com.entities.common.common_entities.Records;
import com.todoapp.todoapp.Kafka.Store.OrderResponseStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class OrderResponseListener {

    private final ObjectMapper objectMapper;
    private final OrderResponseStore responseStore;

    public OrderResponseListener(
            ObjectMapper objectMapper,
            OrderResponseStore responseStore
    ) {
        log.info(String.valueOf(responseStore.hashCode()));
        this.objectMapper = objectMapper;
        this.responseStore = responseStore;
    }

    @KafkaListener(
            topics = "order-response-topic",
            groupId = "users-service"
    )
    public void handleResponse(String message) {
        try {
            Records.OrderResponseEvent response =
                    objectMapper.readValue(message, Records.OrderResponseEvent.class);

            responseStore.complete(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

