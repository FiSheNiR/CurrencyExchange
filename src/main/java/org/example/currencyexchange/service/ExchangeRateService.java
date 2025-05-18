package org.example.currencyexchange.service;

import org.example.currencyexchange.dao.CurrencyDao;
import org.example.currencyexchange.dao.ExchangeRateDao;
import org.example.currencyexchange.dto.ExchangeRateRequestDto;
import org.example.currencyexchange.dto.ExchangeRateResponseDto;
import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.NotFoundException;
import org.example.currencyexchange.utils.MappingUtils;
import org.example.currencyexchange.utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public ExchangeRate getExchangeRateByCode(String code) {
        ValidationUtils.validateExchangeRate(code);
        return exchangeRateDao.findExchangeRateByCodes(code.substring(0,3), code.substring(3,6)).
                orElseThrow(() -> new NotFoundException("ExchangeRate with code '" + code + "' not found"));
    }

    public ExchangeRate update(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findCurrencyByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDao.findCurrencyByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());

        return exchangeRateDao.update(exchangeRate)
                .orElseThrow(() -> new NotFoundException(
                        "Failed to update exchange rate '" + baseCurrencyCode + "' - '" + targetCurrencyCode + "', no such exchange rate found")
                );
    }

    public List<ExchangeRateResponseDto> getExchangeRates() {

        List<ExchangeRate> exchangeRates = exchangeRateDao.findAllExchangeRates();

        return exchangeRates.stream()
                .map(MappingUtils::convertToDto)
                .collect(Collectors.toList());

    }
}
