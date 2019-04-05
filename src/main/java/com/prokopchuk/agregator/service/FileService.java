package com.prokopchuk.agregator.service;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.exception.WrongIncomingDataException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service to upload files with currency rates in application
 *
 * @author N.Prokopchuk
 */
public interface FileService {
    List<CurrencyDTO> processData(MultipartFile file)
            throws WrongIncomingDataException;
}
