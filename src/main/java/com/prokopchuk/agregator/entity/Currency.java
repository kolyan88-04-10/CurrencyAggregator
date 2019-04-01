package com.prokopchuk.agregator.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private static List<Currency> cash = new ArrayList<>();

    private Currency() {
    }
    private Currency(String name) {
        this.name = name;
    }

    public static Currency getInstance(String name) {
        return cash.stream()
                .filter(currency -> currency.name.equalsIgnoreCase(name))
                .findAny().orElseGet(() -> addToCash(name));
    }

    private static Currency addToCash(String name) {
        Currency currency = new Currency(name);
        cash.add(currency);
        return currency;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return Objects.equals(name, currency.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
