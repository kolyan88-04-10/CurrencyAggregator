package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service to to work with currency entities
 *
 * @author N.Prokopchuk
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepo currencyRepo;

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepo.findAll();
    }

    @Override
    public Currency getByName(String name) {
        return currencyRepo.getByName(name);
    }

    @Override
    public Integer getLastOrder() {
        return currencyRepo.getLastOrder();
    }

    @Override
    public Currency save(Currency currency) {
        return currencyRepo.save(currency);
    }
}
