package org.example.currencyexchange.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private Integer code;

    private String message;
}
