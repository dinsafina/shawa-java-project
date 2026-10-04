package com.base.jackson;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponse {

    private Integer id;
    private String status;
    private Double price;
}
