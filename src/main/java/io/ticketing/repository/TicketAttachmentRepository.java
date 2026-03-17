package io.ticketing.repository;

import io.ticketing.model.TicketAttachmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TicketAttachmentRepository  extends ReactiveCrudRepository<TicketAttachmentEntity, String> {
}
