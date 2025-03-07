package com.kacper.swiftapi.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.repository.SwiftCodeRepository;
import com.kacper.swiftapi.utils.SwiftCodeParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SwiftCodeService {

    private final SwiftCodeRepository repository;
    private final SwiftCodeParser swiftCodeParser;

    public SwiftCodeService(SwiftCodeRepository repository, SwiftCodeParser swiftCodeParser) {
        this.repository = repository;
        this.swiftCodeParser = swiftCodeParser;
    }

    public List<SwiftCode> getAllSwiftCodes() {
        return repository.findAll();
    }

    public Optional<SwiftCode> getSwiftCodeById(Long id) {
        return repository.findById(id);
    }

    public Optional<SwiftCode> getSwiftCodeByCode(String code) {
        return repository.findByCode(code);
    }

    public SwiftCode createSwiftCode(SwiftCode swiftCode) {
        return repository.save(swiftCode);
    }

    public SwiftCode updateSwiftCode(Long id, SwiftCode updatedSwiftCode) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCode(updatedSwiftCode.getCode());
                    existing.setBankName(updatedSwiftCode.getBankName());
                    existing.setCountry(updatedSwiftCode.getCountry());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("SWIFT code not found with id: " + id));
    }

    public void deleteSwiftCode(Long id) {
        repository.deleteById(id);
    }

    public void importSwiftCodesFromExcel(MultipartFile file) throws IOException, InvalidFormatException,
            org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        List<SwiftCode> swiftCodes = swiftCodeParser.parseExcelFile(file.getInputStream());
        repository.saveAll(swiftCodes);
    }
}
