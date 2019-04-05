package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.support.WrongIncomingDataException;

import java.util.List;

/**
 * Service to to work with ExchangeRates entities
 *
 * @author N.Prokopchuk
 */
public interface ExchangeRatesService {
    CurrencyDTO persistCurrency (CurrencyDTO currencyDTO)
            throws WrongIncomingDataException;
    List<CurrencyDTO> getSpecificCurrency(String currencyShortName,
                                          boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataException;
    List<CurrencyDTO> editCurrencyRateByBank(
            String bankName, String shortName, String action, Boolean allow, boolean delete)
            throws WrongIncomingDataException;

    List<CurrencyDTO> getAllExchangeRates();

    List<CurrencyDTO> removeCurrency(String bankName, String currency);
}
