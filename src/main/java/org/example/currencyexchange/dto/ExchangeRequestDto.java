package org.example.currencyexchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRequestDto {

    private String baseCurrencyCode;

    private String targetCurrencyCode;

    private BigDecimal amount;

}
