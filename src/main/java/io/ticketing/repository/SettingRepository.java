package io.ticketing.repository;

import io.ticketing.model.SettingEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SettingRepository extends ReactiveCrudRepository<SettingEntity, UUID> {
    Mono<String> findByValueType(String key, UUID tenantId, UUID channelId, UUID categoryId);
}
