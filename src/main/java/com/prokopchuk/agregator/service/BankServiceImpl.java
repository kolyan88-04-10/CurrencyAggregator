package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.CurrencyTransactionType;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.repository.BankRepository;
import com.prokopchuk.agregator.repository.CurrencyValueRepository;
import com.prokopchuk.agregator.repository.NationalCurrencyRepo;
import com.prokopchuk.agregator.support.StaticMessages;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {

    private static final Logger LOG = LoggerFactory.getLogger(BankServiceImpl.class);
    private static final Comparator<ExchangeRate> dateComparator =
            Comparator.comparing(x -> x.getChanged());

    @Autowired
    private NationalCurrencyRepo nationalCurrencyRepo;
    @Autowired
    private BankRepository bankRepo;
    @Autowired
    private CurrencyValueRepository currencyValueRepository;

    public BankServiceImpl(BankRepository bankRepo) {
        this.bankRepo = bankRepo;
    }

//    //@Override
//    public List<CurrencyDTO> getSpecificCurrency(String currencyShortName, boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataExeption {
//        List<ExchangeRate> result;
//        Currency nationalCurrency = nationalCurrencyRepo.getByName(currencyShortName);
//        if (nationalCurrency==null) {
//            String message = String.format(StaticMessages.MESSAGE_ILLEGAL_CURRENCY_NAME, currencyShortName);
//            LOG.info(message);
//            throw new WrongIncomingDataExeption(message);
//        }
//        CurrencyTransactionType transactionType = isBuying ? CurrencyTransactionType.BUYING : CurrencyTransactionType.SELLING;
//
//        if (ascendByPrice) {
//            result = currencyValueRepository.getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueAsc(nationalCurrency, transactionType, false, true);
//        } else {
//            result = currencyValueRepository.getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueDesc(nationalCurrency, transactionType, false, true);
//        }
//
//        return result.stream().map(this::convert).collect(Collectors.toList());
//    }

    @Override
    public CurrencyDTO persistCurrency(CurrencyDTO newCurrencyDTO) throws WrongIncomingDataExeption {
        Currency currency = nationalCurrencyRepo.getByName(newCurrencyDTO.getName());
        if (currency==null){
            Currency newOne = new Currency();
            newOne.setName(newCurrencyDTO.getName());
            newOne.setChanged(new Date());
            newOne.setDisabled(false);
            newOne.setOrder(nationalCurrencyRepo.getLastOrder());
            currency = nationalCurrencyRepo.save(newOne);
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
            Optional<ExchangeRate> valueOptional = currencyValueRepository
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
            List<ExchangeRate> previousList = currencyValueRepository.getByCurrencyAndBankAndTransactionTypeAndDisabled(currency, bank, transactionType, false);

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

            //historyService.persistHistory(null, toPersist);
            ExchangeRate result = currencyValueRepository.save(toPersist);
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

//    @Transactional
//    //@Override
//    public void persistCurrencyList(List<ExchangeRate> valueList) {
//        LOG.info("Saving list to the database");
//
//        currencyValueRepository.saveAll(valueList);
//    }

    @Override
    public List<Bank> getCurrencysRates() {
        return bankRepo.findAll();
    }
}
