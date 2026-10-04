package com.base.jackson;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {

    private Integer recipeId;
    private Integer quantity;

    @Builder.Default
    private String comment = "Как можно скорее";

}
