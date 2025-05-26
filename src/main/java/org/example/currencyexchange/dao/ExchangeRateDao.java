package org.example.currencyexchange.dao;

import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.DatabaseOperationException;
import org.example.currencyexchange.exeption.EntityExistsException;
import org.example.currencyexchange.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {

    private final String FIND_ALL_EXCHANGE_RATES_QUERY = "SELECT" +
            " er.id AS id," +
            " bc.id AS base_id," +
            " bc.code AS base_code," +
            " bc.full_name AS base_name," +
            " bc.sign AS base_sign," +
            "    tc.id AS target_id," +
            "    tc.code AS target_code," +
            "    tc.full_name AS target_name," +
            "    tc.sign AS target_sign," +
            "    er.rate AS rate" +
            "  FROM currency_exchange.currency_storage.exchange_rates er" +
            "  JOIN currency_exchange.currency_storage.currencies bc ON er.base_currency_id = bc.id" +
            "  JOIN currency_exchange.currency_storage.currencies tc ON er.target_currency_id = tc.id" +
            " ORDER BY id";

    private final String SAVE_EXCHANGE_RATE_QUERY = "INSERT INTO currency_exchange.currency_storage.exchange_rates (base_currency_id, target_currency_id, rate) " +
            "VALUES (?, ?, ?) RETURNING id";

    private final String FIND_EXCHANGE_RATE_BY_CODES = "SELECT" +
            " er.id AS id," +
            " bc.id AS base_id," +
            " bc.code AS base_code," +
            " bc.full_name AS base_name," +
            " bc.sign AS base_sign," +
            "    tc.id AS target_id," +
            "    tc.code AS target_code," +
            "    tc.full_name AS target_name," +
            "    tc.sign AS target_sign," +
            "    er.rate AS rate" +
            "  FROM currency_exchange.currency_storage.exchange_rates er" +
            "  JOIN currency_exchange.currency_storage.currencies bc ON er.base_currency_id = bc.id" +
            "  JOIN currency_exchange.currency_storage.currencies tc ON er.target_currency_id = tc.id" +
            " WHERE (base_currency_id = (SELECT base_currency.id FROM currency_exchange.currency_storage.currencies base_currency WHERE base_currency.code = ?)" +
            "AND target_currency_id = (SELECT target_currency.id FROM currency_exchange.currency_storage.currencies target_currency WHERE target_currency.code = ?))";

    private final String UPDATE_QUERY = "UPDATE currency_exchange.currency_storage.exchange_rates " +
                    "SET rate = ? " +
                    "WHERE base_currency_id = ? AND target_currency_id = ? " +
                    "RETURNING id";

    public List<ExchangeRate> findAllExchangeRates() {
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_EXCHANGE_RATES_QUERY)) {

            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to read currencies from the database");
        }
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SAVE_EXCHANGE_RATE_QUERY)) {

            ps.setLong(1, exchangeRate.getBaseCurrency().getId());
            ps.setLong(2, exchangeRate.getTargetCurrency().getId());
            ps.setBigDecimal(3, exchangeRate.getRate());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new DatabaseOperationException("Failed to save exchange rate " + exchangeRate.getBaseCurrency().getCode() + " to "+ exchangeRate.getTargetCurrency().getCode()+" to the database");
            }

            exchangeRate.setId(rs.getLong("id"));
            return exchangeRate;
        }
        catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new EntityExistsException("Exchange rate "
                        + exchangeRate.getBaseCurrency().getCode() + " to "
                        + exchangeRate.getTargetCurrency().getCode()+ " already exists");
            }
            throw new DatabaseOperationException("Failed to save exchange rate "
                    + exchangeRate.getBaseCurrency().getCode() + " to "
                    + exchangeRate.getTargetCurrency().getCode()+" to the database"
            + e.getMessage());
        }
    }

    public Optional<ExchangeRate> findExchangeRateByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_EXCHANGE_RATE_BY_CODES)) {

            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ResultSet resultSet = ps.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(getExchangeRate(resultSet));

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to read Exchange rate with code " + baseCurrencyCode + targetCurrencyCode + " from the database " + e.getMessage());
        }
    }

    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)) {

            ps.setBigDecimal(1, exchangeRate.getRate());
            ps.setLong(2, exchangeRate.getBaseCurrency().getId());
            ps.setLong(3, exchangeRate.getTargetCurrency().getId());

            ps.execute();
            ResultSet resultSet = ps.getResultSet();

            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong("id"));
                return Optional.of(exchangeRate);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update exchange rate with id '" + exchangeRate.getId() + "' in the database " + e.getMessage());
        }
    }


    private ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),

                new Currency(
                        resultSet.getLong("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getLong("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),

                resultSet.getBigDecimal("rate")
        );
    }
}
