package com.shawarmashop.tests.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {

    private Long id;
    private String name;
    private String unit;
    private Integer onHand;
    private Integer minLevel;
    private SupplierInfo supplier;
}
