package com.prokopchuk.agregator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prokopchuk.agregator.entity.ExchangeRate;
import lombok.Getter;
import lombok.Setter;

/**
 * Main DTO used for data transfer in application
 *
 * @author N.Prokopchuk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class CurrencyDTO {
    public CurrencyDTO(String name, String bank,
                       String transactionType, String value, Boolean allowed) {
        this.name = name;
        this.bank = bank;
        this.transactionType = transactionType;
        this.value = value;
        this.allowed = allowed;
    }

    private String name;
    private String bank;
    private String transactionType;
    private String value;
    private Boolean allowed;

    public CurrencyDTO(ExchangeRate exchangeRate) {
        this.name = exchangeRate.getCurrency().getName();
        this.bank = exchangeRate.getBank().getName();
        this.transactionType = exchangeRate.getTransactionType().name();
        this.value = exchangeRate.getValue().toPlainString();
        this.allowed = exchangeRate.getOperationAllowed();
    }

    public CurrencyDTO() {
    }
}
