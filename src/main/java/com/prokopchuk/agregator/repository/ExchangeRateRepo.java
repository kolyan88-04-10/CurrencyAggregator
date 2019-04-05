package com.prokopchuk.agregator.repository;

import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.CurrencyTransactionType;
import com.prokopchuk.agregator.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * ExchangeRate repository
 *
 * @author N.Prokopchuk
 */
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate,Long> {
    List<ExchangeRate> getByCurrencyAndBankAndTransactionTypeAndDisabled(
            Currency currency, Bank bank, CurrencyTransactionType transactionType, boolean disabled);
    List<ExchangeRate> getByCurrencyAndBankAndTransactionTypeAndDisabledAndOperationAllowed(
            Currency currency, Bank bank, CurrencyTransactionType transactionType,boolean disabled, boolean operationAllowed);
    List<ExchangeRate> getByCurrencyAndBankAndDisabled(Currency currency, Bank bank, boolean disabled);
    List<ExchangeRate> getByBankAndTransactionTypeAndDisabled(
            Bank bank, CurrencyTransactionType transactionType, boolean disabled);
    List<ExchangeRate> getByBankAndCurrencyAndDisabled(Bank bank, Currency currency, boolean disabled);
    List<ExchangeRate> getByBankAndDisabled(Bank bank, boolean disabled);
    List<ExchangeRate> getByCurrencyAndTransactionTypeAndDisabledAndOperationAllowedOrderByValueAsc(
            Currency currency, CurrencyTransactionType transactionType, boolean disabled, boolean operationAllowed);
    List<ExchangeRate> getByCurrencyAndTransactionTypeAndDisabledAndOperationAllowedOrderByValueDesc(
            Currency currency, CurrencyTransactionType transactionType, boolean disabled, boolean operationAllowed);
    List<ExchangeRate> getByDisabled(boolean disabled);
}
