package org.example.currencyexchange.dao;

import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.exeption.DatabaseOperationException;
import org.example.currencyexchange.exeption.EntityExistsException;
import org.example.currencyexchange.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private final String FIND_ALL_CURRENCY_QUERY = "SELECT * FROM currency_exchange.currency_storage.currencies";
    private final String FIND_CURRENCY_BY_CODE_QUERY = "select * from currency_exchange.currency_storage.currencies where code = ?";
    private final String FIND_CURRENCY_BY_ID_QUERY = "select * from currency_exchange.currency_storage.currencies where id = ?";
    private final String SAVE_CURRENCY_QUERY = "INSERT INTO currency_exchange.currency_storage.currencies(code, full_name, sign) VALUES(?, ?, ?)";

    public List<Currency> findAllCurrency(){
        try (Connection con = DataSource.getConnection();
        PreparedStatement ps = con.prepareStatement(FIND_ALL_CURRENCY_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }

            return currencies;
        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to read currencies from the database");
        }
    }

    public Optional<Currency> findCurrencyByCode(String code){
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_CURRENCY_BY_CODE_QUERY)) {
            ps.setString(1, code);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();

            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(getCurrency(resultSet));

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to read currency with code '" + code + "' from the database");
        }
    }

    public Optional<Currency> findCurrencyById(Long id){
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_CURRENCY_BY_ID_QUERY)) {
            ps.setLong(1, id);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(getCurrency(resultSet));

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to read currency with id '" + id + "' from the database");
        }
    }

    public Currency saveCurrency(Currency currency){
        try (Connection con = DataSource.getConnection();
        PreparedStatement ps = con.prepareStatement(SAVE_CURRENCY_QUERY, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());

            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (!rs.next()) {
                throw new DatabaseOperationException("Failed to save currency with code '" + currency.getCode() + "' to the database");
            }

            return getCurrency(rs);
        } catch (SQLException e){
            if ("23505".equals(e.getSQLState())) {
                throw new EntityExistsException("Currency with code '" + currency.getCode() + "' already exists");
            }
            throw new DatabaseOperationException("Failed to save currency with code '" + currency.getCode() + "' to the database");
        }
    }

    private Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
