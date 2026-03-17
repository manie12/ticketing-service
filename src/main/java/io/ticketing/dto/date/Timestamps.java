package io.ticketing.dto.date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class Timestamps {
        private OffsetDateTime openedAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime lastActivityAt;

        public OffsetDateTime getOpenedAt() { return openedAt; }
        public Timestamps setOpenedAt(OffsetDateTime openedAt) { this.openedAt = openedAt; return this; }

        public OffsetDateTime getCreatedAt() { return createdAt; }
        public Timestamps setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

        public OffsetDateTime getUpdatedAt() { return updatedAt; }
        public Timestamps setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public OffsetDateTime getLastActivityAt() { return lastActivityAt; }
        public Timestamps setLastActivityAt(OffsetDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; return this; }
    }