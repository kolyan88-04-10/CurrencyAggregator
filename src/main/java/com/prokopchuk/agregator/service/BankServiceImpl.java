package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.repository.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepo bankRepo;

    @Override
    public List<Bank> getAllBanks() {
        return bankRepo.findAll();
    }
}
