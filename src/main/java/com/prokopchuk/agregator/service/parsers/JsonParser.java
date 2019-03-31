package com.prokopchuk.agregator.service.parsers;

import com.prokopchuk.agregator.entity.Bank;
import org.springframework.web.multipart.MultipartFile;

public class JsonParser implements Parser {
    @Override
    public Bank parse(MultipartFile file) {
        return null;
    }
}
