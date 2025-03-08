package com.kacper.swiftapi.utils;

import com.kacper.swiftapi.entity.SwiftCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
            SwiftCode swiftCodeObj = new SwiftCode();
            String code = getCellValueAsString(row.getCell(0)).trim().toUpperCase();
            String bankName = getCellValueAsString(row.getCell(1)).trim();
            String country = getCellValueAsString(row.getCell(2)).trim().toUpperCase();
            String address = "";
            if (row.getPhysicalNumberOfCells() > 3) {
                address = getCellValueAsString(row.getCell(3)).trim();
            }
            swiftCodeObj.setSwiftCode(code);
            swiftCodeObj.setBankName(bankName);
            swiftCodeObj.setAddress(address);
            swiftCodeObj.setCountryISO2(country);
            swiftCodeObj.setCountryName(country);
            boolean isHeadquarter = code.endsWith("XXX");
            swiftCodeObj.setHeadquarter(isHeadquarter);
            swiftCodes.add(swiftCodeObj);
        }
        workbook.close();
        return swiftCodes;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING)
            return cell.getStringCellValue();
        else if (cell.getCellType() == CellType.NUMERIC)
            return String.valueOf((int) cell.getNumericCellValue());
        else if (cell.getCellType() == CellType.BOOLEAN)
            return String.valueOf(cell.getBooleanCellValue());
        else
            return "";
    }
}
