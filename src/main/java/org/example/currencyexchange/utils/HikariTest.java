package org.example.currencyexchange.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HikariTest {
    public static void main(String[] args) {
        try (Connection con = DataSource.getConnection(); Statement stmt = con.createStatement()) {
            System.out.println(con.getTransactionIsolation());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
