package com.Homeworks.Homework4;

import static com.Homeworks.Homework4.OrderValidator.validateAndPrice;

public class Checkpoint {

    private static void runCase(
            long recipeId,
            RecipeSize size,
            int qty,
            PaymentChoice payment) {
        try {
            int totalPrice = validateAndPrice(recipeId, size, qty, payment);
            System.out.println("[OK] price=" + totalPrice);
        } catch (OrderValidationException e) {
            System.out.println("[REJECT] " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        runCase(0, RecipeSize.M, 2, PaymentChoice.CARD);
        runCase(42, null, 2, PaymentChoice.CARD);
        runCase(42, RecipeSize.L, 15, PaymentChoice.CARD);
        runCase(42, RecipeSize.L, 2, null);
        runCase(42, RecipeSize.L, 5, PaymentChoice.CASH);
        runCase(42, RecipeSize.L, 2, PaymentChoice.CARD);
    }
}
