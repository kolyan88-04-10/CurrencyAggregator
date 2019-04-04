package com.prokopchuk.agregator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SelectCurrencyDTO {
    private String name;
    private String bank;
    private Boolean isBuying;
    private Boolean isAscending;
}
