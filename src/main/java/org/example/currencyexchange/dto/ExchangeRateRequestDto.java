package org.example.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.currencyexchange.enitity.Currency;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {

    private String baseCurrencyCode;

    private String targetCurrencyCode;

    private BigDecimal rate;
}
