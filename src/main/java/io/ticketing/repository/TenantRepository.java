package io.ticketing.repository;

import io.ticketing.model.TenantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TenantRepository extends ReactiveCrudRepository<TenantEntity, UUID> {
    Mono<TenantEntity> findById(UUID id);

}
