package io.ticketing.repository;


import io.ticketing.model.TicketEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketRepository  extends ReactiveCrudRepository<TicketEntity, UUID> {
    Mono<Boolean> existsByTenantIdAndChannelCodeAndRequestId(UUID tenantId, String channelId, String requestId);
    Mono<TicketEntity> findByTenantIdAndChannelCodeAndRequestId(UUID tenantId, String channelId, String requestId);

}
