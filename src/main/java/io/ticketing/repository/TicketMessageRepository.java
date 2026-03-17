package io.ticketing.repository;

import io.ticketing.model.TicketMessageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketMessageRepository extends ReactiveCrudRepository<TicketMessageEntity, String> {


}
