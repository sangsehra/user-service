package com.todoapp.todoapp.Kafka.Producer;

import com.entities.common.common_entities.Records;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "order-request-topic";


    public OrderRequestProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void requestOrders(Records.OrderRequestEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    TOPIC,
                    event.requestId(),
                    payload
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("❌ KAFKA SEND FAILED");
                    ex.printStackTrace();
                } else {
                    System.out.println("✅ KAFKA SEND SUCCESS: " + payload);
                }
            });

        } catch (Exception ex) {
            throw new RuntimeException("Failed to publish order request", ex);
        }
    }

}
