package com.todoapp.todoapp.Controller;

import com.entities.common.common_entities.Records;
import com.todoapp.todoapp.Kafka.Producer.OrderRequestProducer;
import com.todoapp.todoapp.Kafka.Store.OrderResponseStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserOrderController {

    private final OrderRequestProducer producer;
    private final OrderResponseStore responseStore;

    public UserOrderController(
            OrderRequestProducer producer,
            OrderResponseStore responseStore
    ) {
        log.info("this is response store hash {} ",String.valueOf(responseStore.hashCode()));
        this.producer = producer;
        this.responseStore = responseStore;
    }

    @GetMapping("/{userId}/orders")
    public Records.OrderResponseEvent getOrders(@PathVariable Long userId)
            throws InterruptedException {


        // 1️⃣ Create correlation ID
        String requestId = UUID.randomUUID().toString();

        // 2️⃣ Register response future
        CompletableFuture<Records.OrderResponseEvent> future =
                responseStore.create(requestId);

         // 3️⃣ Send Kafka request
        producer.requestOrders(
                new Records.OrderRequestEvent(requestId, userId)
        );
        return responseStore.waitFor(future);
    }
}
