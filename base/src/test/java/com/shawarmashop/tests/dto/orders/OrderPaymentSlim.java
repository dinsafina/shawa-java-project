package com.shawarmashop.tests.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentSlim {

    private String method;
    private Integer amount;
    private String txnId;
    private String status;
}
