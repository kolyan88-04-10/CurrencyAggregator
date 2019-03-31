package com.prokopchuk.agregator.service.parsers;

import com.prokopchuk.agregator.entity.Bank;
import org.springframework.web.multipart.MultipartFile;

public interface Parser {
    Bank parse (MultipartFile file);
}
