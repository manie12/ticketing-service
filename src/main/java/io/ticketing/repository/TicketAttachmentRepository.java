package io.ticketing.repository;

import io.ticketing.model.TicketAttachmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TicketAttachmentRepository  extends ReactiveCrudRepository<TicketAttachmentEntity, UUID> {
}
