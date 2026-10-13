package com.shawarmashop.tests.db.repository;

import com.shawarmashop.tests.db.Jdbc;
import com.shawarmashop.tests.db.RowMapper;
import com.shawarmashop.tests.db.TestDatabase;
import com.shawarmashop.tests.db.rows.OrderRow;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

    private final Jdbc jdbc = TestDatabase.jdbc();

    /**Переиспользуемый маппер строк БД в OrderRow для всех запросов к таблице recipes.
     * Берет колонку из БД и создает Java- объект*/
    static final RowMapper<OrderRow> MAPPER = rs -> OrderRow.builder()
            .id(rs.getLong("id"))
            .recipeId(rs.getLong("recipe_id"))
            .qty(rs.getInt("qty"))
            .totalPrice(rs.getBigDecimal("total_price"))
            .status(rs.getString("status"))
            .placedAt(rs.getTimestamp("placed_at").toInstant())
            // .etaAt(rs.getTimestamp("eta_at").toInstant())
            //.completedAt(rs.getTimestamp("completed_at").toInstant())
            .breadBatchId(rs.getString("bread_batch_id"))
            .paymentMethod(rs.getString("payment_method"))
            .build();

    private static final String SELECT_ALL =
            "SELECT id, recipe_id, qty, total_price, status, placed_at, eta_at, completed_at,"
                    + "       bread_batch_id, payment_method"
                    + "  FROM orders ";

    /**Достает из БД по id сущность OrderRow*/
    public Optional<OrderRow> findById(long id) {
        return jdbc.queryForObject(SELECT_ALL + "WHERE id = ?", MAPPER, id);
    }

    /**Поиск заказов по определенному статусу*/
    public List<OrderRow> findByStatus(String status) {
        return jdbc.queryForList(SELECT_ALL + "WHERE status = ? ORDER BY id", MAPPER, status);
    }

    /***/
    public long countByStatus(String status) {
        return jdbc.queryForObject(
                "SELECT COUNT(*) AS c FROM orders WHERE status = ?",
                rs -> rs.getLong("c"), status).orElse(0L);
    }

    /**Ожидание определенного статуса у сущности*/
    public OrderRow awaitStatus(long orderId, String expected, Duration timeout) {
        Awaitility.await("заказ " + orderId + " -> " + expected)
                .atMost(timeout)
                .pollInterval(Duration.ofMillis(250))
                .until(() -> findById(orderId)
                        .map(row -> expected.equals(row.getStatus()))
                        .orElseThrow(() -> new AssertionError("Заказ " + orderId + " отсутствует в БД")));
        return findById(orderId).orElseThrow();
    }
}
