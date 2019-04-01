package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.repository.BankRepository;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService {
    private BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public void createCurrencyRate(Bank bank) {
        bankRepository.save(bank);
    }
}
