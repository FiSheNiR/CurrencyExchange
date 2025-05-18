package org.example.currencyexchange.dto;

import lombok.*;
import org.example.currencyexchange.enitity.Currency;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRateRequestDto {

    private String baseCurrencyCode;

    private String targetCurrencyCode;

    private BigDecimal rate;
}
