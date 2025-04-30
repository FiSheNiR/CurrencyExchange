package org.example.currencyexchange.enitities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private Integer id;
    private String code;

    @JsonProperty("name")
    private String fullName;

    private String sign;

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
