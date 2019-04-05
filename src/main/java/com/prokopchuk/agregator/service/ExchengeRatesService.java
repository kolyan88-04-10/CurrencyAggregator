package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.support.WrongIncomingDataException;

import java.util.List;

public interface ExchengeRatesService {
    CurrencyDTO persistCurrency (CurrencyDTO currencyDTO)
            throws WrongIncomingDataException;
    List<CurrencyDTO> getSpecificCurrency(String currencyShortName,
                                          boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataException;
    List<CurrencyDTO> changeSpecificCurrencyAllowanceByBank(
            String bankName, String shortName, String action, Boolean allow, boolean delete)
            throws WrongIncomingDataException;

    List<CurrencyDTO> getAllExchangeRates();

    List<CurrencyDTO> removeCurrency(String bankName, String currency);
}
