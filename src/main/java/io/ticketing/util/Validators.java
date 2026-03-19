package io.ticketing.util;

import io.ticketing.datatype.*;
import io.ticketing.datatype.status.CustomerStatus;
import io.ticketing.datatype.status.TenantStatus;
import io.ticketing.dto.attachments.Attachment;
import io.ticketing.dto.category.Category;
import io.ticketing.dto.channel.Channel;
import io.ticketing.dto.customer.Customer;
import io.ticketing.exception.TicketException;
import io.ticketing.model.CategoryEntity;
import io.ticketing.model.ChannelEntity;
import io.ticketing.model.CustomerEntity;
import io.ticketing.model.TenantEntity;
import io.ticketing.repository.CategoryRepository;
import io.ticketing.repository.ChannelRepository;
import io.ticketing.repository.CustomerRepository;
import io.ticketing.repository.TenantRepository;
import io.ticketing.repository.TicketRepository;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Component

public class Validators {

    private static final long MAX_ATTACHMENT_BYTES = 10L * 1024L * 1024L; // 10MB

    private final SharedUtils sharedUtils;

    private final TenantRepository tenantRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    public Validators(
            SharedUtils sharedUtils,
            TenantRepository tenantRepository,
            ChannelRepository channelRepository,
            CategoryRepository categoryRepository,
            CustomerRepository customerRepository,
            TicketRepository ticketRepository
    ) {
        this.sharedUtils = sharedUtils;
        this.tenantRepository = tenantRepository;
        this.channelRepository = channelRepository;
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
    }

    // ----------------------------
    // Bean Validation (Jakarta)
    // ----------------------------

    public Mono<String> validateBody(Object body) {
        log.info("[validateBody] Starting bean validation for body: {}", body != null ? body.getClass().getSimpleName() : "null");
        return Mono.fromSupplier(() -> this.validateInput(body))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public String validateInput(Object body) {
        AtomicReference<String> res = new AtomicReference<>();
        if (body != null) {
            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                factory.getValidator()
                        .validate(body)
                        .stream()
                        .findAny()
                        .ifPresent(e -> {
                            log.warn("[validateInput] Bean validation failed: message={}, body={}",
                                    e.getMessage(), this.sharedUtils.toJson(body, true));
                            res.set(e.getMessage());
                        });
                if (res.get() == null) {
                    log.info("[validateInput] Bean validation passed for body: {}", body.getClass().getSimpleName());
                }
            } catch (Exception e) {
                log.error("[validateInput] Bean validation threw exception for body: {}", body, e);
                res.set("Validate body error");
            }
        } else {
            log.warn("[validateInput] Request body is null");
            res.set("Request body is required");
        }
        return res.get();
    }

    // ----------------------------
    // Business validations (TicketException)
    // These are now mapped by GlobalExceptionHandler -> handleTicketException
    // ----------------------------

    public Mono<TenantEntity> validateTenant(@NonNull UUID tenantId) {
        log.info("[validateTenant] Validating tenant: tenantId={}", tenantId);
        return tenantRepository.findById(tenantId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[validateTenant] Tenant not found: tenantId={}", tenantId);
                    return Mono.error(TicketException.of(TicketErrorType.TENANT_NOT_FOUND));
                }))
                .flatMap(t -> {
                    String status = safeUpper(t.getStatus());
                    if (!TenantStatus.ACTIVE.name().equalsIgnoreCase(status)) {
                        log.warn("[validateTenant] Tenant inactive: tenantId={}, status={}", tenantId, status);
                        return Mono.error(TicketException.of(TicketErrorType.TENANT_INACTIVE));
                    }
                    log.info("[validateTenant] Tenant validated successfully: tenantId={}, status={}", tenantId, status);
                    return Mono.just(t);
                })
                .doOnError(ex -> log.error("[validateTenant] Validation failed: tenantId={}, error={}", tenantId, ex.getMessage()));
    }

    public Mono<Channel> validateChannel(@NonNull UUID tenantId, @NonNull String channelCode) {
        String code = safeUpper(channelCode);
        log.info("[validateChannel] Validating channel: tenantId={}, channelCode={}", tenantId, code);

        if (!AllowedChannelCodes.isAllowed(code)) {
            log.warn("[validateChannel] Channel code not in allowed list: tenantId={}, channelCode={}", tenantId, code);
            return Mono.error(TicketException.of(TicketErrorType.INVALID_CHANNEL));
        }

        return channelRepository.findByTenantIdAndCode(tenantId, code)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[validateChannel] Channel not found in DB: tenantId={}, channelCode={}", tenantId, code);
                    return Mono.error(TicketException.of(TicketErrorType.CHANNEL_NOT_FOUND));
                }))
                .flatMap(entity -> {
                    if (!Boolean.TRUE.equals(entity.getIsEnabled())) {
                        log.warn("[validateChannel] Channel disabled: tenantId={}, channelCode={}, isEnabled={}", tenantId, code, entity.getIsEnabled());
                        return Mono.error(TicketException.of(TicketErrorType.CHANNEL_DISABLED));
                    }
                    log.info("[validateChannel] Channel validated successfully: tenantId={}, channelCode={}", tenantId, code);
                    return Mono.just(mapToChannel(entity));
                })
                .doOnError(ex -> log.error("[validateChannel] Validation failed: tenantId={}, channelCode={}, error={}", tenantId, code, ex.getMessage()));
    }

    private Channel mapToChannel(ChannelEntity entity) {
        Channel channel = new Channel();
        channel.setCode(entity.getCode());
        return channel;
    }

    public Mono<Category> validateCategoryIfPresent(@NonNull UUID tenantId, String categoryCodeOrNull, @NonNull String channelCode) {
        String categoryCode = safeUpper(categoryCodeOrNull);
        if (categoryCode == null) {
            log.info("[validateCategoryIfPresent] No category provided, skipping: tenantId={}", tenantId);
            return Mono.empty();
        }

        final String ch = safeUpper(channelCode);
        log.info("[validateCategoryIfPresent] Validating category: tenantId={}, categoryCode={}, channelCode={}", tenantId, categoryCode, ch);

        return categoryRepository.findByTenantIdAndCode(tenantId, categoryCode)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[validateCategoryIfPresent] Category not found: tenantId={}, categoryCode={}", tenantId, categoryCode);
                    return Mono.error(TicketException.of(TicketErrorType.INVALID_CATEGORY));
                }))
                .flatMap(cat -> {
                    if (!Boolean.TRUE.equals(cat.getIsActive())) {
                        log.warn("[validateCategoryIfPresent] Category inactive: tenantId={}, categoryCode={}, isActive={}", tenantId, categoryCode, cat.getIsActive());
                        return Mono.error(TicketException.of(TicketErrorType.CATEGORY_INACTIVE));
                    }

                    if (cat.getAllowedChannelCodes() != null && !cat.getAllowedChannelCodes().isEmpty()) {
                        boolean allowed = cat.getAllowedChannelCodes().stream()
                                .map(this::safeUpper)
                                .anyMatch(x -> x != null && x.equals(ch));
                        if (!allowed) {
                            log.warn("[validateCategoryIfPresent] Category not allowed for channel: tenantId={}, categoryCode={}, channelCode={}, allowedChannels={}",
                                    tenantId, categoryCode, ch, cat.getAllowedChannelCodes());
                            return Mono.error(TicketException.of(TicketErrorType.CATEGORY_NOT_ALLOWED_FOR_CHANNEL));
                        }
                    }
                    log.info("[validateCategoryIfPresent] Category validated successfully: tenantId={}, categoryCode={}", tenantId, categoryCode);
                    return Mono.just(mapToCategory(cat));
                })
                .doOnError(ex -> log.error("[validateCategoryIfPresent] Validation failed: tenantId={}, categoryCode={}, error={}", tenantId, categoryCode, ex.getMessage()));
    }

    private Category mapToCategory(CategoryEntity entity) {
        Category category = new Category();
        category.setCode(entity.getCode());
        return category;
    }

    public Mono<Customer> validateCustomer(@NonNull UUID tenantId, @NonNull String customerEmail) {
        log.info("[validateCustomer] Validating customer: tenantId={}, customerEmail={}", tenantId, customerEmail);
        return customerRepository.findByTenantIdAndEmail(tenantId, customerEmail)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[validateCustomer] Customer not found: tenantId={}, customerEmail={}", tenantId, customerEmail);
                    return Mono.error(TicketException.of(TicketErrorType.CUSTOMER_NOT_FOUND));
                }))
                .flatMap(c -> {
                    String status = safeUpper(c.getStatus());
                    if (!CustomerStatus.ACTIVE.name().equalsIgnoreCase(status)) {
                        log.warn("[validateCustomer] Customer inactive: tenantId={}, customerEmail={}, status={}", tenantId, customerEmail, status);
                        return Mono.error(TicketException.of(TicketErrorType.CUSTOMER_INACTIVE));
                    }
                    log.info("[validateCustomer] Customer validated successfully: tenantId={}, customerEmail={}", tenantId, customerEmail);
                    return Mono.just(mapToCustomer(c));
                })
                .doOnError(ex -> log.error("[validateCustomer] Validation failed: tenantId={}, customerEmail={}, error={}", tenantId, customerEmail, ex.getMessage()));
    }

    private Customer mapToCustomer(CustomerEntity entity) {
        Customer customer = new Customer();
        customer.setEmail(entity.getEmail());
        customer.setPhoneE164(entity.getPhoneE164());
        return customer;
    }

    public Mono<Void> validateRequestIdUniqueness(@NonNull UUID tenantId,
                                                  @NonNull String channelCode,
                                                  @NonNull String requestId) {
        log.info("[validateRequestIdUniqueness] Checking uniqueness: tenantId={}, channelCode={}, requestId={}", tenantId, channelCode, requestId);

        if (this.sharedUtils.isNullOrEmptyOrBlank(requestId)) {
            log.warn("[validateRequestIdUniqueness] Request ID is blank or null: tenantId={}, channelCode={}", tenantId, channelCode);
            return Mono.error(TicketException.of(TicketErrorType.INVALID_REQUEST_ID));
        }

        return ticketRepository.existsByTenantIdAndChannelCodeAndRequestId(tenantId, channelCode, requestId)
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("[validateRequestIdUniqueness] Duplicate request detected: tenantId={}, channelCode={}, requestId={}", tenantId, channelCode, requestId);
                        return Mono.<Void>error(TicketException.of(TicketErrorType.DUPLICATE_REQUEST));
                    }
                    log.info("[validateRequestIdUniqueness] Request ID is unique: tenantId={}, channelCode={}, requestId={}", tenantId, channelCode, requestId);
                    return Mono.<Void>empty();
                })
                .doOnError(ex -> log.error("[validateRequestIdUniqueness] Uniqueness check failed: tenantId={}, channelCode={}, requestId={}, error={}",
                        tenantId, channelCode, requestId, ex.getMessage()));
    }

    public Mono<Void> validatePriorityIfPresent(String priorityOrNull) {
        String p = safeUpper(priorityOrNull);
        if (p == null) {
            log.info("[validatePriorityIfPresent] No priority provided, skipping");
            return Mono.empty();
        }
        log.info("[validatePriorityIfPresent] Validating priority: priority={}", p);
        if (!AllowedPriorities.isAllowed(p)) {
            log.warn("[validatePriorityIfPresent] Invalid priority: priority={}", p);
            return Mono.error(TicketException.of(TicketErrorType.INVALID_PRIORITY));
        }
        log.info("[validatePriorityIfPresent] Priority validated successfully: priority={}", p);
        return Mono.empty();
    }

    public Mono<Void> validateAttachmentsIfPresent(java.util.List<Attachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            log.info("[validateAttachmentsIfPresent] No attachments provided, skipping");
            return Mono.empty();
        }

        log.info("[validateAttachmentsIfPresent] Validating {} attachment(s)", attachments.size());

        return Mono.fromRunnable(() -> {
            for (int i = 0; i < attachments.size(); i++) {
                Attachment a = attachments.get(i);
                log.info("[validateAttachmentsIfPresent] Validating attachment [{}/{}]: path={}, provider={}, size={}",
                        i + 1, attachments.size(), a.getTempStoragePath(), a.getStorageProvider(), a.getFileSizeBytes());

                if (this.sharedUtils.isNullOrEmptyOrBlank(a.getTempStoragePath())) {
                    log.warn("[validateAttachmentsIfPresent] Attachment [{}/{}] has blank tempStoragePath", i + 1, attachments.size());
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }
                String provider = AllowedStorageProviders.normalize(a.getStorageProvider());
                if (!AllowedStorageProviders.isAllowed(provider)) {
                    log.warn("[validateAttachmentsIfPresent] Attachment [{}/{}] has invalid storageProvider: {}", i + 1, attachments.size(), a.getStorageProvider());
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }

                Long size = a.getFileSizeBytes();
                if (size == null || size < 0) {
                    log.warn("[validateAttachmentsIfPresent] Attachment [{}/{}] has invalid fileSize: {}", i + 1, attachments.size(), size);
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }
                if (size > MAX_ATTACHMENT_BYTES) {
                    log.warn("[validateAttachmentsIfPresent] Attachment [{}/{}] exceeds max size: size={}, max={}", i + 1, attachments.size(), size, MAX_ATTACHMENT_BYTES);
                    throw TicketException.of(TicketErrorType.ATTACHMENT_TOO_LARGE);
                }
            }
            log.info("[validateAttachmentsIfPresent] All {} attachment(s) validated successfully", attachments.size());
        });
    }


    // ----------------------------
    // Helpers
    // ----------------------------

    private String safeUpper(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        return t.toUpperCase(Locale.ROOT);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}

