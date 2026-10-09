package com.shawarmashop.tests.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecipeSlim {

    private Long id;
    private String name;
    private String size;
    private Integer price;
}
