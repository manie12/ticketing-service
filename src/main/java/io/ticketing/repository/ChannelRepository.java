package io.ticketing.repository;

import io.ticketing.model.ChannelEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ChannelRepository extends ReactiveCrudRepository<ChannelEntity, UUID> {
    Mono<ChannelEntity> findByTenantIdAndCode(UUID tenantId, String code);
}
