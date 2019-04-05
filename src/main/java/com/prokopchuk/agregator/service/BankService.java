package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Bank;
import java.util.List;

/**
 * Service to to work with bank entities
 *
 * @author N.Prokopchuk
 */
public interface BankService {
    List<Bank> getAllBanks();
    Bank getByName(String name);
    Bank save(Bank bank);
}
