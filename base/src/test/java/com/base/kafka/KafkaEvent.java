package com.base.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaEvent {

    private String type;
    private OrderPlacedEvent payload;
    private String ts;
    private Long id;
}
