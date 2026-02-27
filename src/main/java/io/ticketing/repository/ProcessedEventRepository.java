package io.ticketing.repository;

import io.ticketing.dto.Idempotency.ProcessedEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProcessedEventRepository extends ReactiveCrudRepository<ProcessedEventEntity, String> {

    Mono<Boolean> existsById(String id);
}
