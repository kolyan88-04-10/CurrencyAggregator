package com.prokopchuk.agregator.repository;


import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;

import java.util.List;

public interface CurrencyRepository {
    List<CurrencyDTO> getSpecificCurrency(String currencyShortName, boolean isBuying, boolean ascendByPrice)
            throws WrongIncomingDataExeption;

    CurrencyDTO persistCurrency(CurrencyDTO newValue) throws WrongIncomingDataExeption;

    void persistCurrencyList(List<ExchangeRate> valueList);

    List<CurrencyDTO> changeSpecificCurrencyAllowanceByBank(String bankName, String shortName,
                                                            String action, Boolean allow, boolean delete)
            throws WrongIncomingDataExeption;

    List<ExchangeRate> getAllData();
}
