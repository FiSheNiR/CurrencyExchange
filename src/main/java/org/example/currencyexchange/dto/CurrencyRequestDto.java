package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class CurrencyRequestDto {

    private String name;

    private String code;

    private String sign;
}
