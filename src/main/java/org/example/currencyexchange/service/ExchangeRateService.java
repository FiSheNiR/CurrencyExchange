package org.example.currencyexchange.service;

import org.example.currencyexchange.dao.CurrencyDao;
import org.example.currencyexchange.dao.ExchangeRateDao;
import org.example.currencyexchange.dto.ExchangeRateRequestDto;
import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.NotFoundException;

public class ExchangeRateService {

    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public ExchangeRate save(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findCurrencyByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDao.findCurrencyByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());

        return exchangeRateDao.save(exchangeRate);
    }
}
