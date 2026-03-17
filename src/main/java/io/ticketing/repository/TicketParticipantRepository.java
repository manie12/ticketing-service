package io.ticketing.repository;

import io.ticketing.model.TicketParticipantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TicketParticipantRepository  extends ReactiveCrudRepository<TicketParticipantEntity, String> {
}
