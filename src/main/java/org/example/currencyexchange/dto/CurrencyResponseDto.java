package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponseDto {
    private Long id;
    private String name;
    private String code;
    private String sign;
}
