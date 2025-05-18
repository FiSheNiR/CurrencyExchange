package org.example.currencyexchange.service;

import org.example.currencyexchange.dao.ExchangeRateDao;
import org.example.currencyexchange.dto.ExchangeRateResponseDto;
import org.example.currencyexchange.dto.ExchangeRequestDto;
import org.example.currencyexchange.dto.ExchangeResponseDto;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.NotFoundException;
import org.example.currencyexchange.utils.MappingUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.MathContext.DECIMAL64;

public class ExchangeService {

    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public ExchangeResponseDto exchange(ExchangeRequestDto exchangeRequestDto) {
        ExchangeRate exchangeRate = findExchangeRate(exchangeRequestDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Exchange rate '%s' - '%s' not found in the database",
                                exchangeRequestDto.getBaseCurrencyCode(), exchangeRequestDto.getTargetCurrencyCode()))
                );

        BigDecimal convertedAmount = convertAmountToDecimal(exchangeRequestDto.getAmount(), exchangeRate);

        return ExchangeResponseDto.builder()
                .baseCurrency(MappingUtils.convertToDto(exchangeRate.getBaseCurrency()))
                .targetCurrency(MappingUtils.convertToDto(exchangeRate.getTargetCurrency()))
                .rate(exchangeRate.getRate())
                .amount(exchangeRequestDto.getAmount())
                .convertedAmount(convertedAmount)
                .build();
    }

    private BigDecimal convertAmountToDecimal(BigDecimal amount, ExchangeRate exchangeRate) {
        return amount.multiply(exchangeRate.getRate().setScale(1, RoundingMode.HALF_EVEN));
    }

    private Optional<ExchangeRate> findExchangeRate(ExchangeRequestDto exchangeRequestDto) {
        Optional<ExchangeRate> exchangeRate = findByDirectRate(exchangeRequestDto);

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByReverseRate(exchangeRequestDto);
        }

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByCrossRate(exchangeRequestDto);
        }

        return exchangeRate;
    }

    private Optional<ExchangeRate> findByDirectRate(ExchangeRequestDto exchangeRequestDto) {
        return exchangeRateDao.findExchangeRateByCodes(exchangeRequestDto.getBaseCurrencyCode(), exchangeRequestDto.getTargetCurrencyCode());
    }

    private Optional<ExchangeRate> findByReverseRate(ExchangeRequestDto exchangeRequestDto) {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findExchangeRateByCodes(exchangeRequestDto.getTargetCurrencyCode(), exchangeRequestDto.getBaseCurrencyCode());

        if (exchangeRate.isEmpty()) {
            return exchangeRate;
        }

        ExchangeRate reversedExchangeRate = exchangeRate.get();

        BigDecimal rate = BigDecimal.ONE.divide(reversedExchangeRate.getRate(), DECIMAL64)
                .setScale(6, RoundingMode.HALF_EVEN);

        ExchangeRate directExchangeRate = new ExchangeRate(
                reversedExchangeRate.getTargetCurrency(),
                reversedExchangeRate.getBaseCurrency(),
                rate
        );

        return Optional.of(directExchangeRate);
    }

    private Optional<ExchangeRate> findByCrossRate(ExchangeRequestDto exchangeRequestDto) {
        Optional<ExchangeRate> usdToBaseOptional = exchangeRateDao.findExchangeRateByCodes("USD", exchangeRequestDto.getBaseCurrencyCode());
        Optional<ExchangeRate> usdToTargetOptional = exchangeRateDao.findExchangeRateByCodes("USD", exchangeRequestDto.getTargetCurrencyCode());

        if (usdToBaseOptional.isEmpty() || usdToTargetOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate usdToBase = usdToBaseOptional.get();
        ExchangeRate usdToTarget = usdToTargetOptional.get();

        BigDecimal rate = usdToTarget.getRate().divide(usdToBase.getRate(), DECIMAL64)
                .setScale(6, RoundingMode.HALF_EVEN);

        ExchangeRate directExchangeRate = new ExchangeRate(
                usdToBase.getTargetCurrency(),
                usdToTarget.getTargetCurrency(),
                rate
        );

        return Optional.of(directExchangeRate);
    }
}
