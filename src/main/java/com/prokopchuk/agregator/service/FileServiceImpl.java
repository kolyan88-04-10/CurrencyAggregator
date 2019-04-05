package com.prokopchuk.agregator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.prokopchuk.agregator.dto.CurrencyDTO;
import com.prokopchuk.agregator.exception.WrongIncomingDataException;
import com.prokopchuk.agregator.support.StaticMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Service to upload files with currency rates in application
 *
 * @author N.Prokopchuk
 */
@Service
public class FileServiceImpl implements FileService {

    private final SimpleModule module = new SimpleModule();
    private final CsvSchema schema = CsvSchema.emptySchema().withHeader();
    @Autowired
    private ExchangeRatesService exchangeRatesService;

    private enum Extension {CSV, JSON, XML}

    @Override
    public List<CurrencyDTO> processData(MultipartFile file)
            throws WrongIncomingDataException {
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
            throw new WrongIncomingDataException(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
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
                    XmlMapper xmlMapper = new XmlMapper();
                    valueList = xmlMapper.readValue(
                            xml, new TypeReference<List<CurrencyDTO>>(){});
                    break;
                default:
                    throw new WrongIncomingDataException(StaticMessages.UNKNOWN_FORMAT + format);
            }
        } catch (IOException e) {
            throw new WrongIncomingDataException(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
        }

        List<CurrencyDTO> result = new ArrayList<>();
        for (CurrencyDTO current : valueList) {
            current.setBank(bankName);
            result.add(exchangeRatesService.persistCurrency(current));
        }
        return result;
    }

    private List<CurrencyDTO> read(ObjectMapper mapper, String data) throws IOException {
        List<CurrencyDTO> result;
        mapper.registerModule(module);
        result = mapper.readValue(data, new TypeReference<List<CurrencyDTO>>(){});
        return result;
    }


}
