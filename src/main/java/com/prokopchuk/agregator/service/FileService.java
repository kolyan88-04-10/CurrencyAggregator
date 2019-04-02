package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    public List<CurrencyDTO> processData(MultipartFile file)
            throws WrongIncomingDataExeption;

}
