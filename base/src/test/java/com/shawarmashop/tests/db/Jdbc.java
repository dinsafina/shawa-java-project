package com.shawarmashop.tests.db;


import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class Jdbc {

    private final DataSource dataSource;

    /**
     * Выполняет SQL-запрос и возвращает единственный объект или empty(), если строк нет.
     */
    public <T> Optional<T> queryForObject(String sql, RowMapper<T> mapper, Object... params) {
        List<T> ts = queryForList(sql, mapper, params);
        if (ts.isEmpty()) {
            return Optional.empty();
        }
        if (ts.size() > 1) {
            throw new IllegalStateException("Ожидалась максимум одна строка, получено "
                    + ts.size() + " для " + sql);
        }
        return Optional.of(ts.get(0));
    }

    /**
     * Выполняет SQL-запрос и возвращает количество затронутых строк.
     */
    public int update(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            bind(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw wrap(sql, e);
        }
    }

    /**
     * Вставляет в БД сущность(тестовые данные напр.) и возвращает ее id
     */
    public long insertReturningId(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(preparedStatement, params);
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new IllegalStateException("INSERT не вернул сгенерированный ключ: " + sql);
            }
        } catch (SQLException e) {
            throw wrap(sql, e);
        }
    }

    /**
     * Выполняет SQL-запрос и возвращает список объектов, применяя RowMapper к каждой строке результата
     */
    public <T> List<T> queryForList(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            bind(preparedStatement, params);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapper.mapRow(resultSet));
                }
                return result;
            }
        } catch (SQLException e) {
            throw wrap(sql, e);
        }
    }

    /**
     * Заполнение параметров в sql запросе вместо плейсхолдеров
     */
    private static void bind(PreparedStatement stmt, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    /**
     * Понятное сообщение об ошибке sql
     */
    private static RuntimeException wrap(String sql, SQLException e) {
        return new RuntimeException("JDBC ошибка на: " + sql + " - " + e.getMessage(), e);
    }
}
