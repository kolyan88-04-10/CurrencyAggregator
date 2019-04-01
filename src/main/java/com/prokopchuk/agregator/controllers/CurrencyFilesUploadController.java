package com.prokopchuk.agregator.controllers;

import com.google.gson.*;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.ExchangeRate;
import com.prokopchuk.agregator.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("CurrencyAggregator/upload")
public class CurrencyFilesUploadController {
    private List<Bank> banks;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.create();
    @Autowired
    private BankService bankService;

    @PostMapping
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String[] nameAndFormat = fileName.split("\\.");
        String bankName = nameAndFormat[0];
        String format = nameAndFormat[1];
        Bank bank = new Bank();
        bank.setName(bankName);
        String content = new String(file.getBytes());
        JsonParser jsonParser = new JsonParser();
        JsonArray rates = jsonParser.parse(content).getAsJsonObject().getAsJsonArray("rates");
        List<ExchangeRate> bankRates = new ArrayList<>();
        for(JsonElement element: rates) {
            JsonObject rateObj = element.getAsJsonObject();

            String currencyCode = rateObj.get("name").getAsString();
            double purchase = rateObj.get("purchase").getAsDouble();
            double sale = rateObj.get("sale").getAsDouble();
            Currency currency = new Currency();
            currency.setName(currencyCode);
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(currency);
            exchangeRate.setPurchaseRate(purchase);
            exchangeRate.setSaleRate(sale);
            bankRates.add(exchangeRate);
        }
        bank.setRates(bankRates);
        bankService.createCurrencyRate(bank);
        return "File is upload successfully";
    }

}
