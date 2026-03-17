package io.ticketing.repository;

import io.ticketing.model.TicketEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TicketEventRepository extends ReactiveCrudRepository<TicketEventEntity, String> {
}
