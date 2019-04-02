package com.prokopchuk.agregator.repository;


import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.CurrencyTransactionType;
import com.prokopchuk.agregator.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyValueRepository extends JpaRepository<ExchangeRate,Long> {
    List<ExchangeRate> getByCurrencyAndBankAndTransactionTypeAndDisabled(
            Currency currency, Bank bank, CurrencyTransactionType transactionType, boolean disabled);
    List<ExchangeRate> getByCurrencyAndBankAndTransactionTypeAndDisabledAndOperationAllowed(
            Currency currency, Bank bank, CurrencyTransactionType transactionType, boolean disabled, boolean operationAllowed);
//    List<ExchangeRate> getByTypeAndBankAndDisabled(Currency type, Bank bank, boolean disabled);
//    List<ExchangeRate> getByBankAndSellingValueAndDisabled(
//            Bank bank, CurrencyTransactionType sellingValue, boolean disabled);
//    List<ExchangeRate> getByBankAndDisabled(Bank bank, boolean disabled);
//    List<ExchangeRate> getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueAsc(
//            Currency type, CurrencyTransactionType sellingValue, boolean disabled, boolean operationAllowed);
//    List<ExchangeRate> getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueDesc(
//            Currency type, CurrencyTransactionType sellingValue, boolean disabled, boolean operationAllowed);
//    List<ExchangeRate> getByDisabled(boolean disabled);
}
