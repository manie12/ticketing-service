package io.ticketing.repository;

import io.ticketing.model.TicketEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TicketEventRepository extends ReactiveCrudRepository<TicketEventEntity, UUID> {
}
