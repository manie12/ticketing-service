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
                            log.warn(String.format(
                                    "HTTP validation body failed [ message=%s, body=%s ]",
                                    e.getMessage(),
                                    this.sharedUtils.toJson(body, true)
                            ));
                            res.set(e.getMessage());
                        });
            } catch (Exception e) {
                log.error(String.format("HTTP validation body failed [ %s ]", body), e);
                res.set("Validate body error");
            }
        } else {
            res.set("Request body is required");
        }
        return res.get();
    }

    // ----------------------------
    // Business validations (TicketException)
    // These are now mapped by GlobalExceptionHandler -> handleTicketException
    // ----------------------------

    public Mono<TenantEntity> validateTenant(@NonNull UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.TENANT_NOT_FOUND)))
                .flatMap(t -> {
                    String status = safeUpper(t.getStatus());
                    if (!TenantStatus.ACTIVE.name().equalsIgnoreCase(status)) {
                        return Mono.error(TicketException.of(TicketErrorType.TENANT_INACTIVE));
                    }
                    return Mono.just(t);
                });
    }

    public Mono<Channel> validateChannel(@NonNull UUID tenantId, @NonNull String channelCode) {
        String code = safeUpper(channelCode);

        if (!AllowedChannelCodes.isAllowed(code)) {
            return Mono.error(TicketException.of(TicketErrorType.INVALID_CHANNEL));
        }

        return channelRepository.findByTenantIdAndCode(tenantId, code)
                .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.CHANNEL_NOT_FOUND)))
                .flatMap(entity -> {
                    if (!Boolean.TRUE.equals(entity.getIsEnabled())) {
                        return Mono.error(TicketException.of(TicketErrorType.CHANNEL_DISABLED));
                    }

                    return Mono.just(mapToChannel(entity));
                });
    }

    private Channel mapToChannel(ChannelEntity entity) {
        Channel channel = new Channel();
        channel.setCode(entity.getCode());
        return channel;
    }

    public Mono<Category> validateCategoryIfPresent(@NonNull UUID tenantId, String categoryCodeOrNull, @NonNull String channelCode) {
        String categoryCode = safeUpper(categoryCodeOrNull);
        if (categoryCode == null) return Mono.empty();

        final String ch = safeUpper(channelCode);

        return categoryRepository.findByTenantIdAndCode(tenantId, categoryCode)
                .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.INVALID_CATEGORY)))
                .flatMap(cat -> {
                    if (!Boolean.TRUE.equals(cat.getIsActive())) {
                        return Mono.error(TicketException.of(TicketErrorType.CATEGORY_INACTIVE));
                    }

                    if (cat.getAllowedChannelCodes() != null && !cat.getAllowedChannelCodes().isEmpty()) {
                        boolean allowed = cat.getAllowedChannelCodes().stream()
                                .map(this::safeUpper)
                                .anyMatch(x -> x != null && x.equals(ch));
                        if (!allowed) {
                            return Mono.error(TicketException.of(TicketErrorType.CATEGORY_NOT_ALLOWED_FOR_CHANNEL));
                        }
                    }
                    return Mono.just(mapToCategory(cat));
                });
    }

    private Category mapToCategory(CategoryEntity entity) {
        Category category = new Category();
        category.setCode(entity.getCode());
        return category;
    }

    public Mono<Customer> validateCustomer(@NonNull UUID tenantId, @NonNull String customerEmail) {
        return customerRepository.findByTenantIdAndEmail(tenantId, customerEmail)
                .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.CUSTOMER_NOT_FOUND)))
                .flatMap(c -> {
                    String status = safeUpper(c.getStatus());
                    if (!CustomerStatus.ACTIVE.name().equalsIgnoreCase(status)) {
                        return Mono.error(TicketException.of(TicketErrorType.CUSTOMER_INACTIVE));
                    }
                    return Mono.just(mapToCustomer(c));
                });
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
        if (this.sharedUtils.isNullOrEmptyOrBlank(requestId)) {
            return Mono.error(TicketException.of(TicketErrorType.INVALID_REQUEST_ID));
        }

        return ticketRepository.existsByTenantIdAndChannelCodeAndRequestId(tenantId, channelCode, requestId)
                .flatMap(exists -> exists
                        ? Mono.error(TicketException.of(TicketErrorType.DUPLICATE_REQUEST))
                        : Mono.empty());
    }

    public Mono<Void> validatePriorityIfPresent(String priorityOrNull) {
        String p = safeUpper(priorityOrNull);
        if (p == null) return Mono.empty();
        if (!AllowedPriorities.isAllowed(p)) {
            return Mono.error(TicketException.of(TicketErrorType.INVALID_PRIORITY));
        }
        return Mono.empty();
    }

    public Mono<Void> validateAttachmentsIfPresent(java.util.List<Attachment> attachments) {
        if (attachments == null || attachments.isEmpty()) return Mono.empty();

        return Mono.fromRunnable(() -> {
            for (Attachment a : attachments) {
                if (this.sharedUtils.isNullOrEmptyOrBlank(a.getTempStoragePath())) {
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }
                String provider = AllowedStorageProviders.normalize(a.getStorageProvider());
                if (!AllowedStorageProviders.isAllowed(provider)) {
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }

                Long size = a.getFileSizeBytes();
                if (size == null || size < 0) {
                    throw TicketException.of(TicketErrorType.VALIDATION_ERROR);
                }
                if (size > MAX_ATTACHMENT_BYTES) {
                    throw TicketException.of(TicketErrorType.ATTACHMENT_TOO_LARGE);
                }
            }
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