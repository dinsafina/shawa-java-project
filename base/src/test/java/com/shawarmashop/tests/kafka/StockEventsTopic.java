package com.shawarmashop.tests.kafka;

import com.shawarmashop.tests.kafka.events.StockEvent;

import java.time.Duration;
import java.util.function.Predicate;

public class StockEventsTopic extends KafkaTopic<StockEvent> {

    public static final String TOPIC = "order.events";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    protected StockEventsTopic(String groupId) {
        super(TOPIC, StockEvent.class, groupId);
    }

    /** Ожидает событие RESTOCK_PLACED (пополнение начато) для указанного ингредиента. */
    public StockEvent waitRestockPlaced(long ingredientId) {
        return waitRestockPlaced(ingredientId, DEFAULT_TIMEOUT);
    }

    /** Ожидает событие RESTOCK_PLACED с заданным таймаутом для указанного ингредиента. */
    public StockEvent waitRestockPlaced(long ingredientId, Duration timeout) {
        return awaitMessage("RESTOCK_PLACED", byIngredientId(ingredientId), timeout);
    }

    /** Ожидает событие RESTOCK_FAILED (ошибка пополнения) для указанного ингредиента. */
    public StockEvent waitRestockFailed(long ingredientId) {
        return waitRestockFailed(ingredientId, DEFAULT_TIMEOUT);
    }

    /** Ожидает событие RESTOCK_FAILED с заданным таймаутом для указанного ингредиента. */
    public StockEvent waitRestockFailed(long ingredientId, Duration timeout) {
        return awaitMessage("RESTOCK_FAILED", byIngredientId(ingredientId), timeout);
    }

    /** Ожидает событие STOCK_LOW (критический остаток) для указанного ингредиента. */
    public StockEvent waitStockLow(long ingredientId) {
        return awaitMessage("STOCK_LOW", byIngredientId(ingredientId), DEFAULT_TIMEOUT);
    }

    /** Строит предикат для фильтрации сообщений по ID ингредиента. */
    private static Predicate<StockEvent> byIngredientId(long id) {
        return x -> x.getIngredientId() != null && x.getIngredientId() == id;
    }
}
