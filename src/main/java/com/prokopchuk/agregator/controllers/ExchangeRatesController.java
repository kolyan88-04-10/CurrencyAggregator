package com.prokopchuk.agregator.controllers;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.dto.SelectCurrencyDTO;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.service.BankService;
import com.prokopchuk.agregator.service.CurrencyService;
import com.prokopchuk.agregator.service.ExchengeRatesService;
import com.prokopchuk.agregator.support.WrongIncomingDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CacheResult;
import java.util.List;

@Controller("CurrencyAggregator/rates")
public class ExchangeRatesController {
    @Autowired
    private ExchengeRatesService exchengeRatesService;
    @Autowired
    private BankService bankService;
    @Autowired
    private CurrencyService currencyService;

    @CacheResult(cacheName = "values")
    @GetMapping(path = "CurrencyAggregator/all")
    public String getAllCurrencyRates(Model model){
            model.addAttribute("rates",
                    exchengeRatesService.getAllExchangeRates());
            return "view-all-rates";
    }

//    @CacheRemoveAll(cacheName = "values")
//    @PutMapping(consumes={MediaType.APPLICATION_JSON_UTF8_VALUE},
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public ResponseEntity changeCurrencyAvailability(@RequestBody CurrencyDTO incoming,
//                                                     @RequestParam(value="delete", required = false) Boolean delete){
//        String bankName = incoming.getBank();
//        String currencyName = incoming.getName();
//        String action = incoming.getTransactionType();
//        Boolean allow = incoming.getAllowed();
//        if (allow == null && (delete == null || !delete)){
//            return new ResponseEntity<>(StaticMessages.NO_FLAGS, HttpStatus.BAD_REQUEST);
//        }
//        try {
//            return new ResponseEntity<>(exchengeRatesService.changeSpecificCurrencyAllowanceByBank(
//                    bankName, currencyName, action, allow, delete), HttpStatus.OK);
//        } catch (WrongIncomingDataException e) {
//            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("CurrencyAggregator/create")
    public String showCreateCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchengeRatesService.getAllExchangeRates();
        CurrencyDTO currencyForm = new CurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        return "edit-rates";
    }

    @PostMapping("/CurrencyAggregator/create")
    public String saveCurrency (@ModelAttribute CurrencyDTO currencyDTO) {
        try {
            exchengeRatesService.persistCurrency(currencyDTO);
        } catch (WrongIncomingDataException wrongIncomingDataException) {
            return "error-page";
        }
        return "view-all-rates";
    }

    @GetMapping("CurrencyAggregator/delete")
    public String showDeleteCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchengeRatesService.getAllExchangeRates();
        List<Currency> currencies = currencyService.getAllCurrencies();
        List<Bank> banks = bankService.getAllBanks();
        CurrencyDTO currencyForm = new CurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        model.addAttribute("currencies", currencies);
        model.addAttribute("banks", banks);
        return "delete-rate";
    }

    @DeleteMapping("/CurrencyAggregator/delete")
    public String deleteCurrency (
            @ModelAttribute CurrencyDTO currencyDTO) {
        exchengeRatesService.removeCurrency(currencyDTO.getBank(), currencyDTO.getName());
        return "view-all-rates";
    }

    @GetMapping("CurrencyAggregator/select")
    public String showSelectCurrencyForm(Model model) {
        List<CurrencyDTO> rates = exchengeRatesService.getAllExchangeRates();
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
            List<CurrencyDTO> resultList =  exchengeRatesService.getSpecificCurrency(
                    currencyName, isBuying, isAscending);
            model.addAttribute("rates", resultList);
        } catch (WrongIncomingDataException wrongIncomingDataException) {
            return "error-page";
        }
        return "view-rates";
    }
}
