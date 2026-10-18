package com.shawarmashop.tests.partners.bread_grpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreadOrderPickup {
    private String slot;
    private String courier;
    private boolean refrigerated;
}
