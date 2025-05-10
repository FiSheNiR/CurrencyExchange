package org.example.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.currencyexchange.enitity.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponseDto {

        private CurrencyResponseDto baseCurrency;

        private CurrencyResponseDto targetCurrency;

        private BigDecimal rate;

        private BigDecimal amount;

        private BigDecimal convertedAmount;
}
