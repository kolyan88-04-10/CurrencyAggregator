package com.prokopchuk.agregator.service.parsers;

import com.prokopchuk.agregator.entity.Bank;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileParser {
    Bank parse (MultipartFile file) throws IOException;
}
