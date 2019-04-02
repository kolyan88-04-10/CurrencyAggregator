package com.prokopchuk.agregator.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true, exclude = {"type", "bank"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class ExchangeRate extends AbstractEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private Currency type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @Digits(integer=7, fraction=2)
    private BigDecimal value;
    @Enumerated(EnumType.STRING)
    private CurrencyTransactionType sellingValue;
    private Boolean operationAllowed;
}
