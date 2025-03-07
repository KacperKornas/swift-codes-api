package com.kacper.swiftapi.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.kacper.swiftapi.entity.SwiftCode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class SwiftCodeParser {

    public List<SwiftCode> parseExcelFile(InputStream inputStream) throws IOException, InvalidFormatException {
        List<SwiftCode> swiftCodes = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String countryISO2 = getCellValue(row.getCell(0));
                String swiftCode = getCellValue(row.getCell(1));
                String codeType = getCellValue(row.getCell(2));
                String bankName = getCellValue(row.getCell(3));
                String address = getCellValue(row.getCell(4));
                String townName = getCellValue(row.getCell(5));
                String countryName = getCellValue(row.getCell(6));
                String timeZone = getCellValue(row.getCell(7));

                countryISO2 = countryISO2.toUpperCase();
                countryName = countryName.toUpperCase();
                boolean isHeadquarter = "HQ".equalsIgnoreCase(codeType);

                SwiftCode swiftCodeObj = new SwiftCode();
                swiftCodeObj.setCode(swiftCode);
                swiftCodeObj.setBankName(bankName);
                swiftCodeObj.setCountry(countryISO2);

                swiftCodes.add(swiftCodeObj);
            }
        }
        return swiftCodes;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case ERROR:
            default:
                return "";
        }
    }
}
