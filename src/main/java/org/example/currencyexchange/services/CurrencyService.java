package org.example.currencyexchange.services;

import org.example.currencyexchange.dao.CurrencyDao;
import org.example.currencyexchange.enitities.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<Currency> findAllCurrencies() throws SQLException {
        return currencyDao.findAllCurrency();
    }

    public Optional<Currency> findCurrencyByCode(String code) throws SQLException {
        return currencyDao.findCurrencyByCode(code);
    }

    public Integer addCurrency(Currency currency) throws SQLException {
        return currencyDao.saveCurrency(currency);
    }
}
