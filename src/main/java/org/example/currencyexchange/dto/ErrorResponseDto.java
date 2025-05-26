package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private Integer code;

    private String message;
}
