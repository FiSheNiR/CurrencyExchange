package org.example.currencyexchange.utils;

import org.example.currencyexchange.dto.CurrencyRequestDto;
import org.example.currencyexchange.dto.CurrencyResponseDto;
import org.example.currencyexchange.dto.ExchangeRateResponseDto;
import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.modelmapper.ModelMapper;

public class MappingUtils {

    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(CurrencyRequestDto.class, Currency.class)
                .addMapping(CurrencyRequestDto::getName, Currency::setFullName);
    }

    public static Currency convertDtoToEntity(CurrencyRequestDto currencyRequestDto) {
        return MODEL_MAPPER.map(currencyRequestDto, Currency.class);
    }

    public static CurrencyResponseDto convertToDto(Currency currency) {
        return MODEL_MAPPER.map(currency, CurrencyResponseDto.class);
    }

    public static ExchangeRateResponseDto convertToDto(ExchangeRate exchangeRate) {
        return MODEL_MAPPER.map(exchangeRate, ExchangeRateResponseDto.class);
    }
}
