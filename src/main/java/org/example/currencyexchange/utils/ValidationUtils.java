package org.example.currencyexchange.utils;

import org.example.currencyexchange.dto.CurrencyRequestDto;
import org.example.currencyexchange.dto.ExchangeRateRequestDto;
import org.example.currencyexchange.dto.ExchangeRequestDto;
import org.example.currencyexchange.exeption.InvalidParameterException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    private static Set<String> currencyCodes;

    public static void validateCurrencyRequest(CurrencyRequestDto currencyRequestDto) {
        String code = currencyRequestDto.getCode();
        String name = currencyRequestDto.getName();
        String sign = currencyRequestDto.getSign();

        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Missing parameter - code");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Missing parameter - name");
        }

        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Missing parameter - sign");
        }

        validateCurrencyCode(code);
    }

    public static void validateExchangeRateRequest(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDto.getRate();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - baseCurrencyCode");
        }

        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - targetCurrencyCode");
        }

        if (rate == null) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter - rate cannot be negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    public static void validateExchangeRequest(ExchangeRequestDto exchangeRequestDto) {
        String baseCurrencyCode = exchangeRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDto.getTargetCurrencyCode();
        BigDecimal amount = exchangeRequestDto.getAmount();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - baseCurrencyCode");
        }

        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - targetCurrencyCode");
        }

        if (amount == null) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter - rate cannot be negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    public static void validateExchangeRate(String code){
        if (code.length() != 6) {
            throw new InvalidParameterException("Exchange rate must be contain 6 symbols");
        }

        String baseCurrencyCode = code.substring(0, 3);
        String targetCurrencyCode = code.substring(3, 6);
        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    public static void validateCurrencyCode(String code) {

        if (currencyCodes == null) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            currencyCodes = currencies.stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());
        }

        if (!currencyCodes.contains(code)) {
            throw new InvalidParameterException("Currency code must be in ISO 4217 format");
        }
    }

    public static BigDecimal validateRate(String parameter){
        if (parameter == null || !parameter.contains("rate")) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        String rate = parameter.replace("rate=", "");

        if (rate.isBlank()) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        return convertToNumber(rate);
    }

    public static BigDecimal convertToNumber(String rate) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(rate));
        }
        catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter rate must be a number " + e.getMessage());
        }
    }
}
