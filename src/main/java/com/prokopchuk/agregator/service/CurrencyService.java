package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Currency;
import java.util.List;

/**
 * Service to to work with currency entities
 *
 * @author N.Prokopchuk
 */
public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Currency getByName(String name);
    Integer getLastOrder();
    Currency save(Currency currency);
}
