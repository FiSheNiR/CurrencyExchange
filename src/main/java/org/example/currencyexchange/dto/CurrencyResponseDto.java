package org.example.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseDto {
    private Long id;
    private String name;
    private String code;
    private String sign;
}
