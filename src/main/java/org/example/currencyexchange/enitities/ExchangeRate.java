package org.example.currencyexchange.enitities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate(Integer baseCurrencyId, Integer targetCurrencyId, BigDecimal rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
