package io.ticketing.dto.Ticket;

import io.ticketing.dto.category.Category;
import io.ticketing.dto.channel.Channel;
import io.ticketing.dto.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payload for Create Ticket (data field in HttpResponse<CreateTicketResponse>).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {

    private UUID id;
    private UUID tenantId;

    private String publicId;
    private String requestId;

    private Customer customer;

    private Channel channel;   // ideally UUID in DB; keeping String to match your entity
    private String status;      // e.g. TICKET_CREATED / TICKET_PROCESSING / ...
    private String priority;    // LOW / MEDIUM / HIGH / URGENT

    private Category category;    // nullable

    private String subject;
    private String description;

    private String assigneeRef; // nullable (agent/team reference)

    private OffsetDateTime openedAt;
    private OffsetDateTime closedAt;
    private OffsetDateTime updatedAt;

}
