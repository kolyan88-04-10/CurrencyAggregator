package com.prokopchuk.agregator.service.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.entity.Currency;
import com.prokopchuk.agregator.entity.ExchangeRate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileParser implements FileParser {
    @Override
    public Bank parse(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String[] nameAndFormat = fileName.split("\\.");
        String bankName = nameAndFormat[0];
        String format = nameAndFormat[1];
        Bank bank = new Bank();
        bank.setName(bankName);
        String content = new String(file.getBytes());
        com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
        JsonArray rates = jsonParser.parse(content).getAsJsonObject().getAsJsonArray("rates");
        List<ExchangeRate> bankRates = new ArrayList<>();
        for(JsonElement element: rates) {
            JsonObject rateObj = element.getAsJsonObject();

            String currencyCode = rateObj.get("name").getAsString();
            double purchase = rateObj.get("purchase").getAsDouble();
            double sale = rateObj.get("sale").getAsDouble();
            Currency currency = Currency.getInstance(currencyCode);
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(currency);
            exchangeRate.setPurchaseRate(purchase);
            exchangeRate.setSaleRate(sale);
            bankRates.add(exchangeRate);
        }
        bank.setRates(bankRates);
        return bank;
    }
}
