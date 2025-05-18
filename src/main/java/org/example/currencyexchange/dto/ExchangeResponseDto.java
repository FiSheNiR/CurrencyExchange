package org.example.currencyexchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
public class ExchangeResponseDto {

        private CurrencyResponseDto baseCurrency;

        private CurrencyResponseDto targetCurrency;

        private BigDecimal rate;

        private BigDecimal amount;

        private BigDecimal convertedAmount;
}
