package com.shawarmashop.tests.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private String status;
    private OrderRecipeSlim recipe;
    private Integer qty;
    private Integer totalPrice;
    private Instant placedAt;
    private Instant etaAt;
    private OrderPaymentSlim payment;
}
