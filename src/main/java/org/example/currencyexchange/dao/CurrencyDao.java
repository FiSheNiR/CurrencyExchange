package org.example.currencyexchange.dao;

import org.example.currencyexchange.enitities.Currency;
import org.example.currencyexchange.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    public List<Currency> findAllCurrency() throws SQLException {
        final String query = "SELECT * FROM currency_exchange.currency_storage.currencies";
        try (Connection con = DataSource.getConnection();
        PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }

            return currencies;
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public Optional<Currency> findCurrencyByCode(String code) throws SQLException {
        String query = "select * from currency_exchange.currency_storage.currencies where code = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, code);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(getCurrency(resultSet));

        } catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public Optional<Currency> findCurrencyById(Integer code) throws SQLException {
        String query = "select * from currency_exchange.currency_storage.currencies where id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, code);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(getCurrency(resultSet));

        } catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public Integer saveCurrency(Currency currency) throws SQLException {
        String query = "INSERT INTO currency_exchange.currency_storage.currencies(code, full_name, sign) VALUES(?, ?, ?)";
        try (Connection con = DataSource.getConnection();
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Failed to insert row into currency_storage.currencies");
            }
            return rs.getInt(1);
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
    private Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
