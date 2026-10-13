package com.shawarmashop.tests.db;

public class TestDatabase {

    private static final class Holder{
        private final static Jdbc JDBC = new Jdbc(HikariPool.dataSource());
    }

    public static Jdbc jdbc(){
        return Holder.JDBC;
    }
}
