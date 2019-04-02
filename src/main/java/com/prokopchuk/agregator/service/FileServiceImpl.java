package com.prokopchuk.agregator.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.support.StaticMessages;
import com.prokopchuk.agregator.support.WrongIncomingDataExeption;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final SimpleModule module = new SimpleModule();
    private final CsvSchema schema = CsvSchema.emptySchema().withHeader();
    @Autowired
    private BankService bankService;

    private enum Extension {CSV, JSON, XML}

    @Override
    public List<CurrencyDTO> processData(MultipartFile file)
            throws WrongIncomingDataExeption {
        String[] fileNameSections;
        String bankName;
        String format;
        Extension extension;

        try {
            fileNameSections = file.getOriginalFilename().split(StaticMessages.SPLITTER_REGEX);
            bankName = fileNameSections[0];
            format = fileNameSections[fileNameSections.length - 1].toUpperCase();
            extension = Extension.valueOf(format);
        } catch (RuntimeException e) {
            throw new WrongIncomingDataExeption(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
        }

        List<CurrencyDTO> valueList;
        try {
            switch (extension) {
                case CSV:
                    ObjectMapper mapper = new CsvMapper();
                    MappingIterator<CurrencyDTO> it = mapper.readerFor(CurrencyDTO.class).with(schema)
                            .readValues(file.getBytes());
                    valueList = it.readAll();
                    break;
                case JSON:
                    valueList = read(new ObjectMapper(), new String(file.getBytes()));
                    break;
                case XML:
                    String xml = new String(file.getBytes());
                    JSONObject jObject = XML.toJSONObject(xml);
                    valueList = read(new ObjectMapper(), jObject.toString());
                    break;
                default:
                    throw new WrongIncomingDataExeption(StaticMessages.UNKNOWN_FORMAT + format);
            }
        } catch (IOException e) {
            throw new WrongIncomingDataExeption(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
        }

        List<CurrencyDTO> result = new ArrayList<>();
        for (CurrencyDTO current : valueList) {
            current.setBank(bankName);
            result.add(bankService.persistCurrency(current));
        }
        return result;
    }

    private List<CurrencyDTO> read(ObjectMapper mapper, String data) throws IOException {
        List<CurrencyDTO> result;
        mapper.registerModule(module);
        result = mapper.readValue(data, List.class);
        return result;
    }


}
