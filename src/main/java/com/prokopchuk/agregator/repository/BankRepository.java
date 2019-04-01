package com.prokopchuk.agregator.repository;

import com.prokopchuk.agregator.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
