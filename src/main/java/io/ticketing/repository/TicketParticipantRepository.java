package io.ticketing.repository;

import io.ticketing.model.TicketParticipantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TicketParticipantRepository  extends ReactiveCrudRepository<TicketParticipantEntity, UUID> {
}
