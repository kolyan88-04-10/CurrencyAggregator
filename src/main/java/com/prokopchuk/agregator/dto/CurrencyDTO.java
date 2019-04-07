package com.prokopchuk.agregator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prokopchuk.agregator.entity.ExchangeRate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Main DTO used for data transfer in application
 *
 * @author N.Prokopchuk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter @NoArgsConstructor
public class CurrencyDTO {

    @NotNull()
    @NotBlank()
    @Size(min = 3, max = 3, message = "Currency name must contains 3 symbols")
    private String name;
    @NotNull()
    @NotBlank()
    private String bank;
    private String transactionType;
    @NotNull()
    @NotBlank()
    private String value;
    private Boolean allowed;

    public CurrencyDTO(ExchangeRate exchangeRate) {
        this.name = exchangeRate.getCurrency().getName();
        this.bank = exchangeRate.getBank().getName();
        this.transactionType = exchangeRate.getTransactionType().name();
        this.value = exchangeRate.getValue().toPlainString();
        this.allowed = exchangeRate.getOperationAllowed();
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", value='" + value + '\'' +
                ", allowed=" + allowed +
                '}';
    }
}
