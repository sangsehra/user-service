package com.todoapp.todoapp.Kafka.Store;

import com.entities.common.common_entities.Records;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;



@Component
public class OrderResponseStore {

    private final Map<String, CompletableFuture<Records.OrderResponseEvent>> pendingRequests =
            new ConcurrentHashMap<>();

    private static final long TIMEOUT_SECONDS = 10;

    /**
     * Create and register a future for a requestId
     */
    public CompletableFuture<Records.OrderResponseEvent> create(String requestId) {
        CompletableFuture<Records.OrderResponseEvent> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);
        return future;
    }

    /**
     * Complete the future when Kafka response arrives
     */
    public void complete(Records.OrderResponseEvent response) {
        CompletableFuture<Records.OrderResponseEvent> future =
                pendingRequests.remove(response.requestId());

        if (future != null) {
            future.complete(response);
        }
        // else: response arrived late or already timed out → ignore safely
    }

    /**
     * Wait for response with timeout
     */
    public Records.OrderResponseEvent waitFor(CompletableFuture<Records.OrderResponseEvent> future) {
        try {
            return future.get(
                    TIMEOUT_SECONDS,
                    TimeUnit.SECONDS
            );
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("Timeout waiting for order response");
        } catch (Exception e) {
            throw new RuntimeException("Failed while waiting for order response", e);
        }
    }
}
