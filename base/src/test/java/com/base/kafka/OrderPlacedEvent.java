package com.base.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPlacedEvent {

    private Integer orderId;
    private Integer recipeId;
    private String recipeName;
    private Integer qty;
    private Double totalPrice;
}
