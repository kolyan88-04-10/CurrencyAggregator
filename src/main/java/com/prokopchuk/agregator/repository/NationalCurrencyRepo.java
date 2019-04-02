package com.prokopchuk.agregator.repository;

import com.prokopchuk.agregator.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NationalCurrencyRepo extends JpaRepository<Currency, Integer> {
   Currency getByName(String name);

    @Query(value = "SELECT MAX(order_sequence) FROM nationalcurrency", nativeQuery = true)
    Integer getLastOrder();
}
