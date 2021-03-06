package com.prokopchuk.agregator.repository;

import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Bank repository
 *
 * @author N.Prokopchuk
 */
public interface BankRepo extends JpaRepository<Bank, Long> {
    Bank getByName(String name);
}
