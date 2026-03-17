package io.ticketing.repository;

import io.ticketing.model.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, String> {
    Mono<CustomerEntity> findByTenantIdAndEmail(UUID tenantId, String email);
}
