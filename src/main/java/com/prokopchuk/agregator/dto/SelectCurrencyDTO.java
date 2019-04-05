package com.prokopchuk.agregator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO used in ExchangeRatesController as
 * form to select specified currency and sort order
 *
 * @author N.Prokopchuk
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SelectCurrencyDTO {
    private String name;
    private String bank;
    private Boolean isBuying;
    private Boolean isAscending;
}
