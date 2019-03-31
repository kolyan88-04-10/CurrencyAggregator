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
}
