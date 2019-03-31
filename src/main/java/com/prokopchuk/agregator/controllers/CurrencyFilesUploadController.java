package com.prokopchuk.agregator.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("CurrencyAggregator/upload")
public class CurrencyFilesUploadController {

    @PostMapping
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String content = new String(file.getBytes());
        System.out.println(fileName);
        System.out.println(content);
        return "File is upload successfully";
    }

}
