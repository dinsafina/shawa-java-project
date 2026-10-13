package com.shawarmashop.tests.db.repository;


import com.shawarmashop.tests.db.Jdbc;
import com.shawarmashop.tests.db.RowMapper;
import com.shawarmashop.tests.db.TestDatabase;
import com.shawarmashop.tests.db.rows.RecipeRow;

import java.util.Optional;

public class RecipeRepository {

    private final Jdbc jdbc = TestDatabase.jdbc();

    /**Переиспользуемый маппер строк БД в RecipeRow для всех запросов к таблице recipes.
     * Берет колонку из БД и создает Java- объект*/
    static final RowMapper<RecipeRow> MAPPER = resultSet -> RecipeRow.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .description(resultSet.getString("description"))
                    .size(resultSet.getString("size"))
                    .price(resultSet.getDouble("price"))
                    .prepSeconds(resultSet.getInt("prep_seconds"))
                    .imageUrl(resultSet.getString("image_url"))
                    .build();

    private static final String SELECT_ALL =
            "SELECT id, name, description, size, price, prep_seconds, image_url"
                    + "  FROM recipes ";

    /**Достает по id сущность RecipeRow*/
    public Optional<RecipeRow> findById(long id) {
        return jdbc.queryForObject(SELECT_ALL + "WHERE id = ?", MAPPER, id);
    }

    /**Достает по названию рецепта сущность RecipeRow*/
    public Optional<RecipeRow> findByName(String name) {
        return jdbc.queryForObject(SELECT_ALL + "WHERE name = ?", MAPPER, name);
    }

    /**Кол-во записей внутри таблицы*/
    public long count() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) AS c FROM recipes",
                rs -> rs.getLong("c")).orElse(0L);
    }
}
