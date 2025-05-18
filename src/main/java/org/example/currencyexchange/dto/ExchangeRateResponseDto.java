package org.example.currencyexchange.dto;

import lombok.*;
import org.example.currencyexchange.enitity.Currency;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRateResponseDto {
    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
}
