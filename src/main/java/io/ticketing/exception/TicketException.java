// ========================================
// 1) TicketException
// ========================================
package io.ticketing.exception;

import io.ticketing.datatype.TicketErrorType;
import lombok.Data;
import lombok.Getter;

/**
 * A lightweight business exception that carries TicketErrorType (+ optional data).
 * This allows GlobalExceptionHandler to return the exact business code instead of VALIDATION_ERROR.
 */
@Data
public class TicketException extends RuntimeException {

    private final TicketErrorType errorType;
    private final Object data; // optional (may be null)

    public TicketException(TicketErrorType errorType) {
        this(errorType, null, null);
    }

    public TicketException(TicketErrorType errorType, Object data) {
        this(errorType, data, null);
    }

    public TicketException(TicketErrorType errorType, Object data, Throwable cause) {
        super(errorType != null ? errorType.getStatusMessage() : "Ticket error", cause);
        this.errorType = errorType;
        this.data = data;
    }

    // Convenience factories (optional)
    public static TicketException of(TicketErrorType type) {
        return new TicketException(type);
    }

    public static TicketException of(TicketErrorType type, Object data) {
        return new TicketException(type, data);
    }
}