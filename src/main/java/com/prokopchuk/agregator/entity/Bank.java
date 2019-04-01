package com.prokopchuk.agregator.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "currency_rates",
//    joinColumns = @JoinColumn(name="currency_Id"))
//    @MapKeyJoinColumn(name="currency_Id")
//    @ManyToMany(cascade = CascadeType.ALL)
    private Map<Currency, ExchangeRate> currencyExchangeRate;

    public Bank() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Currency, ExchangeRate> getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public void setCurrencyExchangeRate(Map<Currency, ExchangeRate> currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }
}
