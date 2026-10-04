package com.base.jdbc;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcRowMapperTest {

    private static String URL = "jdbc:postgresql://shawarma.threadqa.ru:5433/shawarma_db";
    private static String USER = "shawarma_reader";
    private static String PASSWORD = "shawara_cucumber_nadzor";

    private final static RowMapper<RecipeRow> RECIPE_MAPPER = resultSet ->
            RecipeRow.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .description(resultSet.getString("description"))
                    .size(resultSet.getString("size"))
                    .price(resultSet.getDouble("price"))
                    .prepSeconds(resultSet.getInt("prep_seconds"))
                    .imageUrl(resultSet.getString("image_url"))
                    .build();

    @SneakyThrows
    private static <T> T queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return mapper.mapRow(resultSet);
            }
        }
    }

    @SneakyThrows
    private static <T> List<T> queryAll(String sql, RowMapper<T> mapper) {
        List<T> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapper.mapRow(resultSet));
                }
            }
        }
        return list;
    }

    @Test
    public void test1() {
        RecipeRow recipeRow = queryOne(
                "select * from recipes where id = ?",
                RECIPE_MAPPER,
                1);
        System.out.println(recipeRow);

        List<RecipeRow> recipeRows = queryAll("select * from recipes", RECIPE_MAPPER);
        System.out.println(recipeRows);

        List<String> strings = queryAll("select name from ingredients", x -> x.getString("name"));
        System.out.println(strings);
    }
}
