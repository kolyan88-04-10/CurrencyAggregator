package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;

import java.util.List;

public interface BankService {
    CurrencyDTO persistCurrency (CurrencyDTO currencyDTO)
            throws WrongIncomingDataExeption;
    List<CurrencyDTO> getSpecificCurrency(String currencyShortName,
                                          boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataExeption;
    List<CurrencyDTO> changeSpecificCurrencyAllowanceByBank(
            String bankName, String shortName, String action, Boolean allow, boolean delete)
            throws WrongIncomingDataExeption;

    List<CurrencyDTO> getAllExchangeRates();

    CurrencyDTO removeCurrency(String bankName, String currency);
}
