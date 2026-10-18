package com.shawarmashop.tests.partners.bread_grpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreadOrderBread {
    private String type;
    private int quantity;
    private String diet;
}