package io.ticketing.repository;

import io.ticketing.dto.Idempotency.ProcessedEventEntity;
import io.ticketing.model.TenantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TenantRepository extends ReactiveCrudRepository<TenantEntity, String> {
    Mono<TenantEntity> findById(UUID id);

}
