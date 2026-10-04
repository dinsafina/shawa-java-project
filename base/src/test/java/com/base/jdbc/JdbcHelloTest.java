package com.base.jdbc;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcHelloTest {

    private static String URL = "jdbc:postgresql://shawarma.threadqa.ru:5433/shawarma_db";
    private static String USER = "shawarma_reader";
    private static String PASSWORD = "shawara_cucumber_nadzor";

    @Test
    @SneakyThrows
    public void test1() {
        String sql = "select * from recipes where id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 15);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String size = resultSet.getString("size");
                    double price = resultSet.getDouble("price");
                    int prepSeconds = resultSet.getInt("prep_seconds");
                    System.out.println("Достали " + name);
                }
            }
        }
    }

    @Test
    @SneakyThrows
    public void test2() {
        String sql = "select * from recipes where id = ?";
        RecipeRow recipeRow;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 15);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    recipeRow = RecipeRow.builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .description(resultSet.getString("description"))
                            .size(resultSet.getString("size"))
                            .price(resultSet.getDouble("price"))
                            .prepSeconds(resultSet.getInt("prep_seconds"))
                            .imageUrl(resultSet.getString("image_url"))
                            .build();
                } else {
                    throw new AssertionError("Заказ с id 15 не найден");
                }
            }
            System.out.println(recipeRow);
            Assertions.assertThat(recipeRow).isNotNull();
            Assertions.assertThat(recipeRow.getImageUrl()).endsWith(".png");
        }
    }
}
