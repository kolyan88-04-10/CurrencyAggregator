package com.prokopchuk.agregator.entity;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import java.util.Map;

//@Entity
public class Bank {
//    @Id
//    @GeneratedValue
    private int id;
    private String name;
    private Map<Currency, ExchangeRate> currencyExchangeRate;

    public Bank() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
