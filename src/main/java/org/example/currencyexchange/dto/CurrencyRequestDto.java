package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRequestDto {

    private String name;

    private String code;

    private String sign;
}
