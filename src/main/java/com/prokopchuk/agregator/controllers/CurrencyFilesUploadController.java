package com.prokopchuk.agregator.controllers;


import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.service.FileService;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import java.io.*;
import java.util.List;

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

        } catch (WrongIncomingDataExeption e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
        }
    }


//    @CacheResult(cacheName = "values")
//    @GetMapping(produces={MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<byte[]> getBetPrices() {
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            fileService.generateReportPdf(outputStream);
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//            byte[] result = StreamUtils.copyToByteArray(inputStream);
//            //byte[] result = IOUtils.toByteArray(inputStream);
//
//            return ResponseEntity
//                    .ok()
//                    .contentLength(result.length)
//                    .contentType(MediaType.parseMediaType("application/octet-stream"))
//                    .header("Content-Disposition", "inline; filename=\"best-prices.pdf\"")
//                    .body(result);
//
//        } catch (IOException e) {
//            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.OK);
//        } catch (com.itextpdf.text.DocumentException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
