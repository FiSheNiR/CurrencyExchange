package org.example.currencyexchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequestDto {

    private String baseCurrencyCode;

    private String targetCurrencyCode;

    private BigDecimal amount;

}
