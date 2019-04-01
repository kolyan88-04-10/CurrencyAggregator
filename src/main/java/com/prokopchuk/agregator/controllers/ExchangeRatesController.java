package com.prokopchuk.agregator.controllers;

import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("CurrencyAggregator/rates")
public class ExchangeRatesController {
    @Autowired
    private BankService bankService;
    @GetMapping()
    public ResponseEntity<Object> getCurrencysRates(){
        return new ResponseEntity<>(bankService.getCurrencysRates(),
                HttpStatus.OK);
    }

}
