package com.prokopchuk.agregator.controllers;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.dto.SelectCurrencyDTO;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.service.BankService;
import com.prokopchuk.agregator.support.StaticMessages;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import java.util.List;

@Controller("CurrencyAggregator/rates")
public class ExchangeRatesController {
    @Autowired
    private BankService bankService;

    @CacheResult(cacheName = "values")
    @GetMapping(path = "CurrencyAggregator/all")
    public String getAllCurrencyRates(Model model){
            model.addAttribute("rates",
                    bankService.getAllExchangeRates());
            return "view-all-rates";
    }

    @CacheResult(cacheName = "values")
    @GetMapping(path = "CurrencyAggregator/rates/{currency}/{isBuying}/{ascendByPrice}")
    public String getSpecificCurrency( Model model,
                                       @PathVariable String currency,
                                       @PathVariable boolean isBuying,
                                       @PathVariable boolean ascendByPrice){
        try {
            model.addAttribute("rates",
                    bankService.getSpecificCurrency(currency, isBuying, ascendByPrice));
            return "view-rates";
        } catch (WrongIncomingDataExeption e) {
            return "error-page";
        }
    }

    @CacheRemoveAll(cacheName = "values")
    @PutMapping(consumes={MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity changeCurrencyAvailability(@RequestBody CurrencyDTO incoming,
                                                     @RequestParam(value="delete", required = false) Boolean delete){
        String bankName = incoming.getBank();
        String currencyName = incoming.getName();
        String action = incoming.getTransactionType();
        Boolean allow = incoming.getAllowed();
        if (allow == null && (delete == null || !delete)){
            return new ResponseEntity<>(StaticMessages.NO_FLAGS, HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(bankService.changeSpecificCurrencyAllowanceByBank(
                    bankName, currencyName, action, allow, delete), HttpStatus.OK);
        } catch (WrongIncomingDataExeption e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @CacheRemoveAll(cacheName = "values")
//    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
//            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public ResponseEntity addSpecificCurrency(@RequestBody CurrencyDTO incoming){
//        try {
//            return new ResponseEntity<>(
//                    bankService.persistCurrency(incoming),HttpStatus.CREATED);
//        } catch (WrongIncomingDataExeption e) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("CurrencyAggregator/create")
    public String showCreateCurrencyForm(Model model) {
        List<CurrencyDTO> rates = bankService.getAllExchangeRates();
        CurrencyDTO currencyForm = new CurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        return "edit-rates";
    }

    @PostMapping("/CurrencyAggregator/create")
    public String saveCurrency (@ModelAttribute CurrencyDTO currencyDTO) {
        try {
            bankService.persistCurrency(currencyDTO);
        } catch (WrongIncomingDataExeption wrongIncomingDataExeption) {
            return "error-page";
        }
        return "view-all-rates";
    }

    @GetMapping("CurrencyAggregator/select")
    public String showSelectCurrencyForm(Model model) {
        List<CurrencyDTO> rates = bankService.getAllExchangeRates();
        SelectCurrencyDTO currencyForm = new SelectCurrencyDTO();
        model.addAttribute("form", currencyForm);
        model.addAttribute("rates", rates);
        return "select-currency";
    }

    @PostMapping("/CurrencyAggregator/show")
    public String showSpecifiedCurrency (Model model,
                                         @ModelAttribute SelectCurrencyDTO selectCurrencyDTO) {
        try {
            String currencyName = selectCurrencyDTO.getName();
            boolean isBuying = selectCurrencyDTO.getIsBuying();
            boolean isAscending = selectCurrencyDTO.getIsAscending();
            List<CurrencyDTO> resultList =  bankService.getSpecificCurrency(
                    currencyName, isBuying, isAscending);
            model.addAttribute("rates", resultList);
        } catch (WrongIncomingDataExeption wrongIncomingDataExeption) {
            return "error-page";
        }
        return "view-rates";
    }
}
