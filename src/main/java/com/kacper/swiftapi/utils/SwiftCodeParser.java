package com.kacper.swiftapi.utils;

import com.kacper.swiftapi.entity.SwiftCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class SwiftCodeParser {

    public List<SwiftCode> parseExcelFile(InputStream is) throws IOException, InvalidFormatException {
        List<SwiftCode> swiftCodes = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        boolean firstRow = true;
        for (Row row : sheet) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            if (isRowEmpty(row)) continue;
            String swiftCode = getCellValueAsString(row.getCell(1)).trim().toUpperCase();
            if (swiftCode.isEmpty()) {
                System.out.println("Empty SWIFT code at row: " + row.getRowNum());
                continue;
            }
            SwiftCode swiftCodeObj = new SwiftCode();
            String countryISO2 = getCellValueAsString(row.getCell(0)).trim().toUpperCase();
            String codeType = getCellValueAsString(row.getCell(2)).trim().toUpperCase();
            boolean isHeadquarter = codeType.contains("HQ") || codeType.equals("HEADQUARTERS");
            String bankName = getCellValueAsString(row.getCell(3)).trim();
            String address = getCellValueAsString(row.getCell(4)).trim();
            String countryName = getCellValueAsString(row.getCell(7)).trim().toUpperCase();
            swiftCodeObj.setCountryISO2(countryISO2);
            swiftCodeObj.setCountryName(countryName);
            swiftCodeObj.setSwiftCode(swiftCode);
            swiftCodeObj.setBankName(bankName);
            swiftCodeObj.setAddress(address);
            swiftCodeObj.setHeadquarter(isHeadquarter);
            swiftCodes.add(swiftCodeObj);
        }
        workbook.close();
        return swiftCodes;
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
}
