package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class CurrencyResponseDto {
    private Long id;
    private String name;
    private String code;
    private String sign;
}
