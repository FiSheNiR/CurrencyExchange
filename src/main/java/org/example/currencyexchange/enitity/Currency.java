package org.example.currencyexchange.enitity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}
