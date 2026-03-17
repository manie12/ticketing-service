package io.ticketing.repository;


import io.ticketing.model.TicketEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketRepository  extends ReactiveCrudRepository<TicketEntity, String> {
    Mono<Boolean> existsByTenantIdAndChannelCodeAndRequestId(UUID tenantId, String channelId, String requestId);
    Mono<TicketEntity> findByTenantIdAndChannelIdAndRequestId(UUID tenantId, UUID channelId, String requestId);

}
