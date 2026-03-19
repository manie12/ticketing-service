package io.ticketing.repository;

import io.ticketing.model.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryEntity, UUID> {
    Mono<CategoryEntity> findByTenantIdAndCode(UUID tenantId, String code);
}
