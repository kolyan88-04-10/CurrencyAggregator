package com.prokopchuk.agregator.entity;


import javax.persistence.*;

@Embeddable
public class ExchangeRate {

    @OneToOne(cascade=CascadeType.ALL)
    private Currency currency;

    private double purchaseRate;
    private double saleRate;

    public ExchangeRate() {
    }

    public double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(double saleRate) {
        this.saleRate = saleRate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
