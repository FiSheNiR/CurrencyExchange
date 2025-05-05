package org.example.currencyexchange.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig("datasource.properties");
    private static final HikariDataSource dataSource = new HikariDataSource(config);

    private DataSource() {}

//    static {
//        try {
//            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl("jdbc:postgresql://localhost:5432/currency_exchange");
//            config.setUsername("postgres");
//            config.setPassword("postgres");
//            //Добавить в datasoirce проперти
//            config.setDriverClassName("org.postgresql.Driver");
//
//            // Настройки пула соединений
//            config.setMaximumPoolSize(10);
//            config.setMinimumIdle(5);
//            config.setConnectionTimeout(30000);
//
//            dataSource = new HikariDataSource(config);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to initialize DataSource", e);
//        }
//    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
