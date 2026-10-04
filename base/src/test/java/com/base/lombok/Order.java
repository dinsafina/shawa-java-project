package com.base.lombok;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class Order {

    private Integer id;
    private String status;
    private Double price;

    private final String orderName;

}
