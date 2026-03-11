package io.ticketing.dto.debezium;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboxEventRow {

    @JsonProperty("id")
    private String id;

    @JsonProperty("aggregate_type")
    private String aggregateType;

    @JsonProperty("aggregate_id")
    private String aggregateId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("payload")
    private String payload;

    @JsonProperty("created_at")
    private Long createdAt;

}
