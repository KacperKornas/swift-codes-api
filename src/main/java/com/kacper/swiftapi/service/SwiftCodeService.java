package com.kacper.swiftapi.service;

import com.kacper.swiftapi.dto.CountrySwiftCodesDto;
import com.kacper.swiftapi.dto.SwiftCodeDto;
import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.repository.SwiftCodeRepository;
import com.kacper.swiftapi.utils.SwiftCodeParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class SwiftCodeService {
    private final SwiftCodeRepository repository;
    private final SwiftCodeParser parser;
    public SwiftCodeService(SwiftCodeRepository repository, SwiftCodeParser parser) {
        this.repository = repository;
        this.parser = parser;
    }
    private SwiftCodeDto convertToDto(SwiftCode swift) {
        SwiftCodeDto dto = new SwiftCodeDto();
        dto.setSwiftCode(swift.getSwiftCode());
        dto.setBankName(swift.getBankName());
        dto.setAddress(swift.getAddress());
        dto.setCountryISO2(swift.getCountryISO2().toUpperCase());
        dto.setCountryName(swift.getCountryName().toUpperCase());
        dto.setHeadquarter(swift.isHeadquarter());
        return dto;
    }
    public SwiftCodeDto getSwiftCodeByCode(String swiftCode) {
        SwiftCode swift = repository.findBySwiftCode(swiftCode).orElseThrow(() -> new RuntimeException("Swift code not found"));
        if (swift.isHeadquarter()) {
            SwiftCodeDto dto = convertToDto(swift);
            String prefix = swiftCode.substring(0, 8);
            List<SwiftCodeDto> branches = repository.findBySwiftCodeStartingWithAndIsHeadquarterFalse(prefix)
                    .stream().map(this::convertToDto).collect(Collectors.toList());
            dto.setBranches(branches);
            return dto;
        }
        return convertToDto(swift);
    }
    public CountrySwiftCodesDto getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCode> swiftCodes = repository.findByCountryISO2(countryISO2.toUpperCase());
        if (swiftCodes.isEmpty()){
            throw new RuntimeException("No swift codes found for country: " + countryISO2);
        }
        CountrySwiftCodesDto dto = new CountrySwiftCodesDto();
        dto.setCountryISO2(swiftCodes.get(0).getCountryISO2().toUpperCase());
        dto.setCountryName(swiftCodes.get(0).getCountryName().toUpperCase());
        dto.setSwiftCodes(swiftCodes.stream().map(this::convertToDto).collect(Collectors.toList()));
        return dto;
    }
    public String createSwiftCode(SwiftCode swiftCode) {
        swiftCode.setCountryISO2(swiftCode.getCountryISO2().toUpperCase());
        swiftCode.setCountryName(swiftCode.getCountryName().toUpperCase());
        repository.save(swiftCode);
        return "Swift code created successfully";
    }
    public String updateSwiftCode(String swiftCode, SwiftCode updatedSwiftCode) {
        SwiftCode existing = repository.findBySwiftCode(swiftCode).orElseThrow(() -> new RuntimeException("Swift code not found"));
        existing.setBankName(updatedSwiftCode.getBankName());
        existing.setAddress(updatedSwiftCode.getAddress());
        existing.setCountryISO2(updatedSwiftCode.getCountryISO2().toUpperCase());
        existing.setCountryName(updatedSwiftCode.getCountryName().toUpperCase());
        existing.setHeadquarter(updatedSwiftCode.isHeadquarter());
        repository.save(existing);
        return "Swift code updated successfully";
    }
    public String deleteSwiftCode(String swiftCode) {
        SwiftCode swift = repository.findBySwiftCode(swiftCode).orElseThrow(() -> new RuntimeException("Swift code not found"));
        repository.delete(swift);
        return "Swift code deleted successfully";
    }
    public void importSwiftCodesFromExcel(MultipartFile file) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        List<SwiftCode> swiftCodes = parser.parseExcelFile(file.getInputStream());
        repository.saveAll(swiftCodes);
    }
}
