package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;

import java.util.List;

public interface BankService {
    CurrencyDTO persistCurrency (CurrencyDTO currencyDTO) throws WrongIncomingDataExeption;
    List<Bank> getCurrencysRates();
}
