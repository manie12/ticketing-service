package io.ticketing.datatype;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Expanded Ticketing business error catalog (50+).
 * <p>
 * Conventions:
 * - code: stable external/business code (string)
 * - statusMessage: internal/ops friendly
 * - customerMessage: safe for UI/customer (no sensitive details)
 * <p>
 * Notes:
 * - Keep codes stable once released.
 * - Prefer category prefixes for easier analytics.
 */
@Slf4j
@ToString
@Getter
public enum TicketErrorType {

    // =========================================================
    // 00 - Success
    // =========================================================
    SUCCESS("00", "Success", "Success"),

    // =========================================================
    // 5xx - Generic / platform failures
    // =========================================================
    VALIDATION_ERROR("402", "Validation Error", "Dear Member, we are unable to complete your transaction due to a system error. K-402"),
    GENERIC_ERROR("407", "Internal Server Error",
            "We are unable to complete your request at the moment. Please try again later. TK-507"),
    SERVICE_UNAVAILABLE("503", "Service Unavailable",
            "Service is temporarily unavailable. Please try again shortly. TK-503"),
    TIMEOUT("504", "Gateway Timeout",
            "Your request took too long to process. Please try again. TK-504"),
    DATABASE_ERROR("P500", "Database operation failed",
            "We are unable to complete your request at the moment. Please try again later. TK-P500"),
    EXTERNAL_DEPENDENCY_ERROR("P501", "External dependency error",
            "We are unable to complete your request at the moment. Please try again later. TK-P501"),
    SERIALIZATION_ERROR("P502", "Serialization error",
            "We are unable to process your request at the moment. Please try again. TK-P502"),
    DESERIALIZATION_ERROR("P503", "Deserialization error",
            "We are unable to process your request at the moment. Please try again. TK-P503"),
    CONFIGURATION_ERROR("P504", "Configuration error",
            "We are unable to complete your request at the moment. Please contact support. TK-P504"),
    RATE_LIMITED("P429", "Rate limit exceeded",
            "Too many requests. Please slow down and try again. TK-P429"),

    // =========================================================
    // T1xx - Tenant validations
    // =========================================================
    TENANT_NOT_FOUND("T100", "Tenant not found",
            "We couldn't find your organization. Please contact support. TK-T100"),
    TENANT_INACTIVE("T101", "Tenant inactive",
            "Your organization is currently inactive. Please contact support. TK-T101"),
    TENANT_SUSPENDED("T102", "Tenant suspended",
            "Your organization is currently suspended. Please contact support. TK-T102"),
    TENANT_DELETED("T103", "Tenant deleted",
            "Your organization is currently unavailable. Please contact support. TK-T103"),
    TENANT_SETTINGS_MISSING("T110", "Tenant settings missing",
            "We are unable to complete your request at the moment. Please contact support. TK-T110"),
    TENANT_LIMIT_REACHED("T120", "Tenant limit reached",
            "Your organization has reached its current limit. Please contact support. TK-T120"),

    // =========================================================
    // C2xx - Channel validations
    // =========================================================
    INVALID_CHANNEL("C200", "Invalid channel",
            "This support channel is invalid. Please try again. TK-C200"),
    CHANNEL_DISABLED("C201", "Channel disabled",
            "This support channel is currently unavailable. Please use a different channel. TK-C201"),
    CHANNEL_NOT_FOUND("C202", "Channel not found",
            "This support channel is unavailable. Please try another channel. TK-C202"),
    CHANNEL_CONFIG_MISSING("C203", "Channel config missing",
            "This support channel is temporarily unavailable. Please try again later. TK-C203"),
    CHANNEL_RATE_LIMITED("C204", "Channel rate limited",
            "Too many requests on this channel. Please try again shortly. TK-C204"),
    CHANNEL_UNSUPPORTED_OPERATION("C205", "Channel unsupported operation",
            "This action is not supported on the current channel. TK-C205"),

    // =========================================================
    // CAT3xx - Category validations
    // =========================================================
    INVALID_CATEGORY("CAT300", "Invalid category",
            "The selected category is not available. Please choose another. TK-CAT300"),
    CATEGORY_INACTIVE("CAT301", "Category inactive",
            "The selected category is currently unavailable. Please choose another. TK-CAT301"),
    CATEGORY_NOT_ALLOWED_FOR_CHANNEL("CAT302", "Category not allowed for channel",
            "The selected category is not supported on this channel. Please choose another. TK-CAT302"),
    CATEGORY_PARENT_INVALID("CAT303", "Category parent invalid",
            "The selected category structure is invalid. Please choose another. TK-CAT303"),

    // =========================================================
    // CU4xx - Customer validations
    // =========================================================
    CUSTOMER_NOT_FOUND("CU400", "Customer not found",
            "We couldn't find your profile. Please verify your details and try again. TK-CU400"),
    CUSTOMER_INACTIVE("CU401", "Customer inactive",
            "Your profile is currently inactive. Please contact support. TK-CU401"),
    CUSTOMER_SUSPENDED("CU402", "Customer suspended",
            "Your profile is currently suspended. Please contact support. TK-CU402"),
    CUSTOMER_EMAIL_INVALID("CU403", "Invalid customer email",
            "Please provide a valid email address. TK-CU403"),
    CUSTOMER_PHONE_INVALID("CU404", "Invalid customer phone",
            "Please provide a valid phone number. TK-CU404"),
    CUSTOMER_IDENTITY_REQUIRED("CU405", "Customer identity required",
            "Please provide your email or phone number to continue. TK-CU405"),
    CUSTOMER_DUPLICATE_EMAIL("CU406", "Duplicate email for tenant",
            "This email is already associated with another profile. Please contact support. TK-CU406"),
    CUSTOMER_DUPLICATE_PHONE("CU407", "Duplicate phone for tenant",
            "This phone number is already associated with another profile. Please contact support. TK-CU407"),

    // =========================================================
    // TK6xx - Ticket create/update validations
    // =========================================================
    INVALID_REQUEST_ID("TK600", "Invalid requestId",
            "Your request could not be processed. Please try again. TK-TK600"),
    DUPLICATE_REQUEST("TK601", "Duplicate requestId",
            "This request was already submitted. Please check your tickets list. TK-TK601"),
    INVALID_PUBLIC_ID("TK602", "Invalid publicId",
            "We couldn't process your ticket reference. Please try again. TK-TK602"),
    INVALID_PRIORITY("TK603", "Invalid priority",
            "Invalid priority selected. Please choose a valid priority. TK-TK603"),
    SUBJECT_REQUIRED("TK604", "Subject required",
            "Please enter a subject for your ticket. TK-TK604"),
    SUBJECT_TOO_LONG("TK605", "Subject too long",
            "Your subject is too long. Please shorten it and try again. TK-TK605"),
    DESCRIPTION_REQUIRED("TK606", "Description required",
            "Please add a description so we can assist you. TK-TK606"),
    DESCRIPTION_TOO_LONG("TK607", "Description too long",
            "Your description is too long. Please shorten it and try again. TK-TK607"),
    INVALID_STATUS("TK608", "Invalid status",
            "Invalid ticket status. Please refresh and try again. TK-TK608"),
    INVALID_STATUS_TRANSITION("TK609", "Invalid status transition",
            "This ticket cannot move to the selected status. TK-TK609"),
    TICKET_NOT_FOUND("TK610", "Ticket not found",
            "We couldn't find that ticket. Please refresh and try again. TK-TK610"),
    TICKET_ALREADY_CLOSED("TK611", "Ticket already closed",
            "This ticket is already closed. TK-TK611"),
    TICKET_LOCKED("TK612", "Ticket locked",
            "This ticket is temporarily locked. Please try again shortly. TK-TK612"),
    TICKET_UPDATE_CONFLICT("TK613", "Ticket update conflict",
            "This ticket was updated elsewhere. Please refresh and try again. TK-TK613"),
    TICKET_TOO_MANY_OPEN("TK614", "Too many open tickets",
            "You have too many open tickets. Please close existing tickets first. TK-TK614"),
    TICKET_CREATE_NOT_ALLOWED("TK615", "Ticket creation not allowed",
            "You are not allowed to create a ticket at this time. TK-TK615"),

    // =========================================================
    // MSG7xx - Message/thread validations
    // =========================================================
    MESSAGE_REQUIRED("MSG700", "Message required",
            "Please enter a message. TK-MSG700"),
    MESSAGE_TOO_LONG("MSG701", "Message too long",
            "Your message is too long. Please shorten it and try again. TK-MSG701"),
    MESSAGE_NOT_FOUND("MSG702", "Message not found",
            "We couldn't find that message. Please refresh and try again. TK-MSG702"),
    INVALID_AUTHOR_TYPE("MSG703", "Invalid author type",
            "We couldn't process your message. Please try again. TK-MSG703"),
    MESSAGE_CREATE_NOT_ALLOWED("MSG704", "Message creation not allowed",
            "You cannot send a message on this ticket at the moment. TK-MSG704"),
    MESSAGE_RATE_LIMITED("MSG705", "Message rate limited",
            "Too many messages sent. Please wait and try again. TK-MSG705"),

    // =========================================================
    // AT7xx - Attachment validations
    // =========================================================
    ATTACHMENT_TOO_LARGE("AT700", "Attachment too large",
            "One of your attachments is too large. Please upload a smaller file. TK-AT700"),
    ATTACHMENT_TYPE_NOT_ALLOWED("AT701", "Attachment type not allowed",
            "One of your attachments has an unsupported file type. TK-AT701"),
    ATTACHMENT_MISSING_STORAGE_PATH("AT702", "Attachment missing storage path",
            "We couldn't process your attachment. Please re-upload and try again. TK-AT702"),
    ATTACHMENT_UPLOAD_FAILED("AT703", "Attachment upload failed",
            "We couldn't upload your attachment. Please try again. TK-AT703"),
    ATTACHMENT_NOT_FOUND("AT704", "Attachment not found",
            "We couldn't find that attachment. Please try again. TK-AT704"),
    ATTACHMENT_VIRUS_DETECTED("AT705", "Attachment blocked (virus detected)",
            "Your attachment could not be accepted. Please upload a different file. TK-AT705"),
    ATTACHMENT_CHECKSUM_MISMATCH("AT706", "Attachment checksum mismatch",
            "We couldn't verify your attachment. Please re-upload and try again. TK-AT706"),
    ATTACHMENT_STORAGE_ERROR("AT707", "Attachment storage error",
            "We couldn't store your attachment. Please try again later. TK-AT707"),
    ATTACHMENT_LIMIT_REACHED("AT708", "Attachment limit reached",
            "You have uploaded too many attachments. Please remove one and try again. TK-AT708"),

    // =========================================================
    // TAG8xx - Tagging validations
    // =========================================================
    INVALID_TAG("TAG800", "Invalid tag",
            "One of the selected tags is invalid. TK-TAG800"),
    TOO_MANY_TAGS("TAG801", "Too many tags",
            "Too many tags selected. Please remove some and try again. TK-TAG801"),
    TAG_ALREADY_APPLIED("TAG802", "Tag already applied",
            "That tag is already applied to this ticket. TK-TAG802"),
    TAG_NOT_FOUND("TAG803", "Tag not found",
            "We couldn't find that tag. Please try again. TK-TAG803"),

    // =========================================================
    // PART9xx - Participants validations
    // =========================================================
    PARTICIPANT_ALREADY_EXISTS("PART900", "Participant already exists",
            "That participant is already on this ticket. TK-PART900"),
    PARTICIPANT_NOT_FOUND("PART901", "Participant not found",
            "We couldn't find that participant. Please try again. TK-PART901"),
    INVALID_PARTICIPANT_ROLE("PART902", "Invalid participant role",
            "Invalid participant role selected. TK-PART902"),
    REQUESTER_REQUIRED("PART903", "Requester required",
            "A ticket must have a requester. TK-PART903"),

    // =========================================================
    // SLA10xx - SLA validations
    // =========================================================
    SLA_POLICY_NOT_FOUND("SLA1000", "SLA policy not found",
            "We couldn't apply SLA at this time. Please try again later. TK-SLA1000"),
    SLA_ALREADY_APPLIED("SLA1001", "SLA already applied",
            "SLA is already applied to this ticket. TK-SLA1001"),
    SLA_COMPUTE_FAILED("SLA1002", "SLA compute failed",
            "We couldn't compute SLA at this time. Please try again later. TK-SLA1002"),
    SLA_BREACH_RECORDED("SLA1003", "SLA breach recorded",
            "This ticket has exceeded its SLA. TK-SLA1003"),

    // =========================================================
    // APPR11xx - Approval validations
    // =========================================================
    APPROVAL_REQUIRED("APPR1100", "Approval required",
            "This ticket requires approval before continuing. TK-APPR1100"),
    APPROVAL_ALREADY_REQUESTED("APPR1101", "Approval already requested",
            "Approval has already been requested for this ticket. TK-APPR1101"),
    APPROVAL_NOT_FOUND("APPR1102", "Approval not found",
            "We couldn't find the approval request. TK-APPR1102"),
    APPROVAL_REJECTED("APPR1103", "Approval rejected",
            "This ticket approval was rejected. TK-APPR1103"),
    APPROVAL_NOT_ALLOWED("APPR1104", "Approval action not allowed",
            "You cannot approve or reject this ticket at the moment. TK-APPR1104"),

    // =========================================================
    // LINK12xx - Ticket linking validations
    // =========================================================
    LINK_NOT_ALLOWED("LINK1200", "Ticket linking not allowed",
            "This ticket cannot be linked at the moment. TK-LINK1200"),
    LINK_SELF_NOT_ALLOWED("LINK1201", "Self link not allowed",
            "A ticket cannot be linked to itself. TK-LINK1201"),
    LINK_ALREADY_EXISTS("LINK1202", "Link already exists",
            "These tickets are already linked. TK-LINK1202"),
    LINK_TICKET_NOT_FOUND("LINK1203", "Linked ticket not found",
            "We couldn't find the ticket to link. TK-LINK1203"),

    // =========================================================
    // OUT13xx - Outbox / CDC / integration validations
    // =========================================================
    OUTBOX_WRITE_FAILED("OUT1300", "Outbox write failed",
            "We couldn't complete your request. Please try again. TK-OUT1300"),
    OUTBOX_EVENT_INVALID("OUT1301", "Outbox event invalid",
            "We couldn't process the request at the moment. TK-OUT1301"),
    OUTBOX_DUPLICATE_EVENT("OUT1302", "Duplicate outbox event",
            "We couldn't process the request at the moment. TK-OUT1302"),

    // =========================================================
    // RULE14xx - Rules / settings validations
    // =========================================================
    RULES_EVALUATION_FAILED("RULE1400", "Rules evaluation failed",
            "We couldn't process your request at the moment. Please try again. TK-RULE1400"),
    SETTINGS_KEY_NOT_FOUND("SET1401", "Settings key not found",
            "We couldn't load required settings. Please contact support. TK-SET1401"),
    SETTINGS_VALUE_INVALID("SET1402", "Settings value invalid",
            "We couldn't load required settings. Please contact support. TK-SET1402");

    private final String code;
    private final String statusMessage;
    private final String customerMessage;

    TicketErrorType(String code, String statusMessage, String customerMessage) {
        this.code = code;
        this.statusMessage = statusMessage;
        this.customerMessage = customerMessage;
    }

    public String formatCustomerMessage(Object... args) {
        try {
            return String.format(this.customerMessage, args);
        } catch (Exception e) {
            log.warn("Failed to format customer message for code={} template={}", code, customerMessage, e);
            return this.customerMessage;
        }
    }

    public String formatStatusMessage(Object... args) {
        try {
            return String.format(this.statusMessage, args);
        } catch (Exception e) {
            log.warn("Failed to format status message for code={} template={}", code, statusMessage, e);
            return this.statusMessage;
        }
    }
}