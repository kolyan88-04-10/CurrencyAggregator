package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.CurrencyTransactionType;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.repository.BankRepo;
import com.prokopchuk.agregator.repository.ExchangeRateRepo;
import com.prokopchuk.agregator.repository.CurrencyRepo;
import com.prokopchuk.agregator.support.StaticMessages;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private static final Logger LOG = LoggerFactory.getLogger(BankServiceImpl.class);
    private static final Comparator<ExchangeRate> dateComparator =
            Comparator.comparing(x -> x.getChanged());

    @Autowired
    private CurrencyRepo currencyRepo;
    @Autowired
    private BankRepo bankRepo;
    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    @Override
    public List<CurrencyDTO> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateRepo.getByDisabled(false);
        List<CurrencyDTO> currencyDTOs = new ArrayList<>();
        exchangeRates.stream().forEach(er ->currencyDTOs.add(new CurrencyDTO(er)));
        return currencyDTOs;
    }

    @Override
    public List<CurrencyDTO> getSpecificCurrency(
            String currencyShortName, boolean isBuying, boolean ascendByPrice)
            throws WrongIncomingDataExeption {
        List<ExchangeRate> result;
        Currency currency = currencyRepo.getByName(currencyShortName);
        if (currency==null) {
            String message = String.format(
                    StaticMessages.MESSAGE_ILLEGAL_CURRENCY_NAME, currencyShortName);
            LOG.info(message);
            throw new WrongIncomingDataExeption(message);
        }
        CurrencyTransactionType transactionType = isBuying
                ? CurrencyTransactionType.BUYING : CurrencyTransactionType.SELLING;

        if (ascendByPrice) {
            result = exchangeRateRepo
                    .getByCurrencyAndTransactionTypeAndDisabledAndOperationAllowedOrderByValueAsc(
                            currency, transactionType, false, true);
        } else {
            result = exchangeRateRepo
                    .getByCurrencyAndTransactionTypeAndDisabledAndOperationAllowedOrderByValueDesc(
                            currency, transactionType, false, true);
        }

        return result.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public CurrencyDTO removeCurrency(String bankName, String currency) {
        return null;
    }

    @Override
    public List<CurrencyDTO> changeSpecificCurrencyAllowanceByBank(
            String bankName, String name, String requestTransactionType,
            Boolean allow, boolean delete) throws WrongIncomingDataExeption {
        Bank bank = bankRepo.getByName(bankName);
        if (bank==null){
            throw new WrongIncomingDataExeption("Can't find bank with name: " + bankName);
        }

        Currency currency = currencyRepo.getByName(name);

        CurrencyTransactionType transactionType = null;

        if (requestTransactionType!=null) {
            try {
                transactionType = CurrencyTransactionType.valueOf(requestTransactionType);
            } catch (IllegalArgumentException e) {
                throw new WrongIncomingDataExeption("Unknown action: " + requestTransactionType);
            }
        }
        List<ExchangeRate> toProcessList;
        if (currency!=null){
            if (transactionType!=null){
                toProcessList = exchangeRateRepo
                        .getByCurrencyAndBankAndTransactionTypeAndDisabled(
                                currency, bank, transactionType, false);
            } else {
                toProcessList = exchangeRateRepo
                        .getByCurrencyAndBankAndDisabled(currency, bank, false);
            }
        } else {
            if (transactionType!=null){
                toProcessList = exchangeRateRepo
                        .getByBankAndTransactionTypeAndDisabled(
                                bank, transactionType, false);
            } else {
                toProcessList = exchangeRateRepo.getByBankAndDisabled(
                        bank, false);
            }
        }
        if (delete) {
            toProcessList.stream().filter(x-> !x.getDisabled()).forEach(x->x.setDisabled(true));
        } else if (allow!=null && allow) {
            toProcessList.stream().filter(x-> !x.getOperationAllowed()).forEach(x->x.setOperationAllowed(true));
        } else if (allow!=null && !allow) {
            toProcessList.stream().filter(ExchangeRate::getOperationAllowed).forEach(x->x.setOperationAllowed(false));
        }
        exchangeRateRepo.saveAll(toProcessList);
        return toProcessList.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public CurrencyDTO persistCurrency(CurrencyDTO newCurrencyDTO) throws WrongIncomingDataExeption {
        Currency currency = currencyRepo.getByName(newCurrencyDTO.getName());
        if (currency==null){
            Currency newOne = new Currency();
            newOne.setName(newCurrencyDTO.getName());
            newOne.setChanged(new Date());
            newOne.setDisabled(false);
            newOne.setOrder(currencyRepo.getLastOrder());
            currency = currencyRepo.save(newOne);
        }

        Bank bank = bankRepo.getByName(newCurrencyDTO.getBank());
        if (bank==null){
            Bank newBank = new Bank();
            newBank.setName(newCurrencyDTO.getBank());
            newBank.setChanged(new Date());
            newBank.setDisabled(false);
            bank = bankRepo.save(newBank);
        }

        CurrencyTransactionType transactionType;
        try {
            transactionType = CurrencyTransactionType.valueOf(newCurrencyDTO.getTransactionType());
        } catch (IllegalArgumentException e) {
            throw new WrongIncomingDataExeption(
                    "Unknown transactionType: "+ newCurrencyDTO.getTransactionType());
        }
        Boolean allowed = newCurrencyDTO.getAllowed();

        BigDecimal value;
        if (newCurrencyDTO.getValue()==null || newCurrencyDTO.getValue().isEmpty()) {
            Optional<ExchangeRate> valueOptional = exchangeRateRepo
                    .getByCurrencyAndBankAndTransactionTypeAndDisabledAndOperationAllowed(
                            currency, bank, transactionType, false, true).stream().min(dateComparator);
            if (valueOptional.isPresent()){
                value = valueOptional.get().getValue();
            } else {
                value = new BigDecimal(0);
            }
        } else if (!newCurrencyDTO.getValue().equals(StaticMessages.EMPTY_VALUE)){
            value = new BigDecimal(newCurrencyDTO.getValue());
        } else {
            value = new BigDecimal(0);
        }

        try {
            List<ExchangeRate> previousList = exchangeRateRepo.getByCurrencyAndBankAndTransactionTypeAndDisabled(currency, bank, transactionType, false);

            for (ExchangeRate currentPrevious : previousList) {
                currentPrevious.setDisabled(true);
            }

            ExchangeRate toPersist = new ExchangeRate();
            toPersist.setBank(bank);
            if (value.intValue()==0){
                toPersist.setOperationAllowed(false);
            } else {
                toPersist.setOperationAllowed(allowed);
            }
            toPersist.setDisabled(false);
            toPersist.setTransactionType(transactionType);
            toPersist.setCurrency(currency);
            toPersist.setValue(value);
            toPersist.setChanged(new Date());

            ExchangeRate result = exchangeRateRepo.save(toPersist);
            return convert(result);

        } catch (RuntimeException e) {
            String message = String.format(StaticMessages.MESSAGE_ILLEGAL_CURRENCY_CREATION, newCurrencyDTO);
            LOG.warn(message, e);
            throw new WrongIncomingDataExeption(message);
        }
    }



    private CurrencyDTO convert(ExchangeRate exchangeRate) {
        return new CurrencyDTO(exchangeRate);
    }

}
