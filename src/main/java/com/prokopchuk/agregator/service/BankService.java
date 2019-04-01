package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Bank;

import java.util.List;

public interface BankService {
    void createCurrencyRate (Bank bank);
    List<Bank> getCurrencysRates();
}
