package org.example.currencyexchange.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

//    private static HikariConfig config = new HikariConfig("datasource.properties");
//    private static HikariDataSource ds = new HikariDataSource(config);
//
//    private DataSource() {}

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/currency_exchange");
            config.setUsername("postgres");
            config.setPassword("postgres");
            config.setDriverClassName("org.postgresql.Driver");

            // Настройки пула соединений
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DataSource", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
