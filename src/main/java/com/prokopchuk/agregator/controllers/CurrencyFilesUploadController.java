package com.prokopchuk.agregator.controllers;

import com.google.gson.*;
import com.prokopchuk.agregator.entity.Bank;
import com.prokopchuk.agregator.service.BankService;
import com.prokopchuk.agregator.service.parsers.FileParser;
import com.prokopchuk.agregator.service.parsers.JsonFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("CurrencyAggregator/upload")
public class CurrencyFilesUploadController {
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.create();
    @Autowired
    private BankService bankService;
    private FileParser parser = new JsonFileParser();

    @PostMapping
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        Bank bank = parser.parse(file);
        bankService.createCurrencyRate(bank);
        return "File is upload successfully";
    }

}
