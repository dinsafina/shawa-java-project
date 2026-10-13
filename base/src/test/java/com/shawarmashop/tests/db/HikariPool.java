package com.shawarmashop.tests.db;

import com.shawarmashop.tests.env.TestEnvironment;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariPool {
    private final static class Holder {

        private final static HikariDataSource INSTANCE = build();

        /**Настройки для подключения к БД*/
        private static HikariDataSource build() {
            TestEnvironment env = TestEnvironment.INSTANCE;
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(env.jdbcUrl());
            cfg.setUsername(env.jdbcUser());
            cfg.setPassword(env.jdbcPassword());
            cfg.setPoolName("ShawarmaTestHikariPool");
            cfg.setMaximumPoolSize(8);
            cfg.setMinimumIdle(1);
            cfg.setConnectionTimeout(10_000);
            return new HikariDataSource(cfg);
        }
    }

    /**JVM загружает Holder и создаёт INSTANCE один раз, при первом вызове*/
    public static DataSource dataSource(){
        return Holder.INSTANCE;
    }
}
