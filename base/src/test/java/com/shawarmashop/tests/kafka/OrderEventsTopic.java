package com.shawarmashop.tests.kafka;

import com.shawarmashop.tests.kafka.events.OrderEvent;

import java.time.Duration;
import java.util.function.Predicate;

    public final class OrderEventsTopic extends KafkaTopic<OrderEvent> {

        public static final String TOPIC = "order.events";
        public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

        public OrderEventsTopic(String groupId) {
            super(TOPIC, OrderEvent.class, groupId);
        }

        /** Ожидает событие ORDER_PLACED (заказ создан) для указанного заказа. */
        public OrderEvent waitPlaced(long orderId) {
            return waitPlaced(orderId, DEFAULT_TIMEOUT);
        }

        /** Ожидает событие ORDER_PLACED с заданным таймаутом для указанного заказа. */
        public OrderEvent waitPlaced(long orderId, Duration timeout) {
            return awaitMessage("ORDER_PLACED", byOrderId(orderId), timeout);
        }

        /** Ожидает событие ORDER_PAID (заказ оплачен) для указанного заказа. */
        public OrderEvent waitPaid(long orderId) {
            return waitPaid(orderId, DEFAULT_TIMEOUT);
        }

        /** Ожидает событие ORDER_PAID с заданным таймаутом для указанного заказа. */
        public OrderEvent waitPaid(long orderId, Duration timeout) {
            return awaitMessage("ORDER_PAID", byOrderId(orderId), timeout);
        }

        /** Ожидает событие ORDER_CANCELLED (заказ отменен) для указанного заказа. */
        public OrderEvent waitCancelled(long orderId) {
            return awaitMessage("ORDER_CANCELLED", byOrderId(orderId), DEFAULT_TIMEOUT);
        }

        /** Ожидает событие ORDER_DONE (заказ готов/выдан) для указанного заказа. */
        public OrderEvent waitDone(long orderId) {
            return waitDone(orderId, DEFAULT_TIMEOUT);
        }

        /** Ожидает событие ORDER_DONE с заданным таймаутом для указанного заказа. */
        public OrderEvent waitDone(long orderId, Duration timeout) {
            return awaitMessage("ORDER_DONE", byOrderId(orderId), timeout);
        }

        /** Ожидает событие BREAD_RESERVATION_FAILED (ошибка резерва хлеба) для указанного заказа. */
        public OrderEvent waitBreadReservationFailed(long orderId) {
            return awaitMessage("BREAD_RESERVATION_FAILED", byOrderId(orderId), DEFAULT_TIMEOUT);
        }

        /** Строит предикат для фильтрации сообщений по ID заказа. */
        private static Predicate<OrderEvent> byOrderId(long orderId) {
            return e -> e.getOrderId() != null && e.getOrderId() == orderId;
        }
    }
