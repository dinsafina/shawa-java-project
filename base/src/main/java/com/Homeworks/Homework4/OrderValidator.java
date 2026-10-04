package com.Homeworks.Homework4;

public class OrderValidator {

    static int validateAndPrice(long recipeId,
                                RecipeSize size,
                                int qty,
                                PaymentChoice payment) {
        if (recipeId <= 0) {
            throw new OrderValidationException("recipeId must be positive");
        }
        if (size == null) {
            throw new OrderValidationException("size is required");
        }
        if (qty < 1 || qty > 10) {
            throw new OrderValidationException("qty must be between 1 and 10");
        }
        if (payment == null) {
            throw new OrderValidationException("payment is required");
        }
        int total = size.getBasePrice() * qty;
        if (total > 1000 && !payment.isOnline()) {
            throw new OrderValidationException("cash not allowed for orders over 1000");
        }
        return total;
    }
}
