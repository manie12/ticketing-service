package io.ticketing.util;

import io.ticketing.datatype.ResponseType;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class Validators {
    private final SharedUtils sharedUtils;

    public Validators(SharedUtils sharedUtils) {
        this.sharedUtils = sharedUtils;
    }


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
                        .stream().findAny().ifPresent(e -> {
                            log.warn(String.format("HTTP validation body failed [ message=%s, body=%s ]", e.getMessage(), this.sharedUtils.toJson(body, true)));
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

    public Mono<String> generateRequestId(@NonNull ResponseType type) {
        UUID uuid = UUID.randomUUID();
        log.info(String.format("Generating request id [ type=%s, requestId=%s ]", type, uuid));
        return Mono.just(uuid.toString());
    }
}
