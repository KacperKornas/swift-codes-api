package com.kacper.swiftapi.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SwiftCodeParser {

    public void parseExcelFile(String filePath) throws IOException, InvalidFormatException {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

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

                System.out.println("SWIFT Code: " + swiftCode);
                System.out.println("Bank Name: " + bankName);
                System.out.println("Country ISO2: " + countryISO2);
                System.out.println("Country Name: " + countryName);
                System.out.println("Is Headquarter: " + isHeadquarter);
                System.out.println("Address: " + address);
                System.out.println("-------------------------------------------------");
            }
        }
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

    public static void main(String[] args) {
        String filePath = System.getenv("SWIFT_DATA_PATH");

        if (filePath == null || filePath.isEmpty()) {
            System.out.println("The file path was not created in the environment variable SWIFT_DATA_PATH.");
            return;
        }

        SwiftCodeParser parser = new SwiftCodeParser();
        try {
            parser.parseExcelFile(filePath);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
