package com.prokopchuk.agregator.controllers;

import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.exception.WrongIncomingDataException;
import com.prokopchuk.agregator.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.cache.annotation.CacheRemoveAll;
import java.util.List;

/**
 * Controller to upload files with currency rates in application
 *
 * @author N.Prokopchuk
 */
@RestController
@RequestMapping("CurrencyAggregator/upload")
public class CurrencyFilesUploadController {

    @Autowired
    private FileService fileService;

    @CacheRemoveAll(cacheName = "values")
    @RequestMapping(method= RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        try {
            List<CurrencyDTO> result = fileService.processData(file);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (WrongIncomingDataException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
        }
    }

}
