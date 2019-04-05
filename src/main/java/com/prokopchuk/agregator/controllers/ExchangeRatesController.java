package com.prokopchuk.agregator.controllers;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.dto.SelectCurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.exception.WrongIncomingDataException;
import com.prokopchuk.agregator.service.BankService;
import com.prokopchuk.agregator.service.CurrencyService;
import com.prokopchuk.agregator.service.ExchangeRatesService;
import com.prokopchuk.agregator.support.StaticMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.cache.annotation.CacheResult;
import java.util.List;

/**
 * Controller to show, select, add and edit currency rates in application
 *
 * @author N.Prokopchuk
 */

@Controller("CurrencyAggregator/rates")
public class ExchangeRatesController {
    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @Autowired
    private BankService bankService;
    @Autowired
    private CurrencyService currencyService;

    @CacheResult(cacheName = "values")
    @GetMapping(path = "CurrencyAggregator/all")
    public String getAllCurrencyRates(Model model){
            model.addAttribute("rates",
                    exchangeRatesService.getAllExchangeRates());
            return "view-all-rates";
    }

    @GetMapping("CurrencyAggregator/create")
    public String showCreateCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchangeRatesService.getAllExchangeRates();
        CurrencyDTO currencyForm = new CurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        return "edit-rates";
    }

    @PostMapping("/CurrencyAggregator/create")
    public String saveCurrency (Model model,
                                @ModelAttribute CurrencyDTO currencyDTO) {

        try {
            exchangeRatesService.persistCurrency(currencyDTO);
        } catch (WrongIncomingDataException wrongIncomingDataException) {
            return "error-page";
        }
        List<CurrencyDTO> rates = exchangeRatesService.getAllExchangeRates();
        model.addAttribute("rates", rates);
        model.addAttribute("message", StaticMessages.EXCHANGE_RATE_MODIFIED_MESSAGE);
        return "view-inform";
    }

    @GetMapping("CurrencyAggregator/delete")
    public String showDeleteCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchangeRatesService.getAllExchangeRates();
        List<Currency> currencies = currencyService.getAllCurrencies();
        List<Bank> banks = bankService.getAllBanks();
        CurrencyDTO currencyForm = new CurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        model.addAttribute("currencies", currencies);
        model.addAttribute("banks", banks);
        return "delete-rates";
    }

    @DeleteMapping("/CurrencyAggregator/delete")
    public String deleteCurrency (Model model,
            @ModelAttribute CurrencyDTO currencyDTO) {
        exchangeRatesService.removeCurrency(currencyDTO.getBank(), currencyDTO.getName());
        List<CurrencyDTO> rates = exchangeRatesService.getAllExchangeRates();
        model.addAttribute("rates", rates);
        model.addAttribute("message", StaticMessages.EXCHANGE_RATE_DELETED_MESSAGE);
        return "view-inform";
    }

    @GetMapping("CurrencyAggregator/select")
    public String showSelectCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchangeRatesService.getAllExchangeRates();
        List<Currency> allCurrencies = currencyService.getAllCurrencies();
        SelectCurrencyDTO currencyForm = new SelectCurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        model.addAttribute("currencies", allCurrencies);
        return "select-currency";
    }

    @PostMapping("/CurrencyAggregator/show")
    public String showSpecifiedCurrency (Model model,
                                         @ModelAttribute SelectCurrencyDTO selectCurrencyDTO) {
        try {
            String currencyName = selectCurrencyDTO.getName();
            boolean isBuying = selectCurrencyDTO.getIsBuying();
            boolean isAscending = selectCurrencyDTO.getIsAscending();
            List<CurrencyDTO> resultList =  exchangeRatesService.getSpecificCurrency(
                    currencyName, isBuying, isAscending);
            model.addAttribute("rates", resultList);
        } catch (WrongIncomingDataException wrongIncomingDataException) {
            return "error-page";
        }
        return "view-rates";
    }
}
