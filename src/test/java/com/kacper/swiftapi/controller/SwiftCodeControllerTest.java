package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.repository.SwiftCodeRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @BeforeEach
    public void setup() {
        swiftCodeRepository.deleteAll();
    }

    @Test
    void testGetAllSwiftCodes() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("SWIFT123");
        swiftCode.setBankName("Bank ABC");
        swiftCode.setCountryISO2("PL");
        swiftCode.setCountryName("PL");
        swiftCode.setAddress("");
        swiftCode.setHeadquarter(false);
        swiftCodeRepository.save(swiftCode);

        mockMvc.perform(get("/v1/swift-codes/SWIFT123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.swiftCode").value("SWIFT123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").value("Bank ABC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countryISO2").value("PL"));
    }

    @Test
    void testCreateSwiftCode() throws Exception {
        String json = "{\"swiftCode\": \"SWIFT456\", \"bankName\": \"Bank XYZ\", \"address\": \"\", \"countryISO2\": \"DE\", \"countryName\": \"DE\", \"isHeadquarter\": false}";
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Swift code created successfully"));
    }

    @Test
    void testUpdateSwiftCode() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("SWIFT123");
        swiftCode.setBankName("Bank ABC");
        swiftCode.setCountryISO2("PL");
        swiftCode.setCountryName("PL");
        swiftCode.setAddress("");
        swiftCode.setHeadquarter(false);
        swiftCode = swiftCodeRepository.save(swiftCode);

        String json = "{\"swiftCode\": \"SWIFT456\", \"bankName\": \"Bank XYZ\", \"address\": \"\", \"countryISO2\": \"DE\", \"countryName\": \"DE\", \"isHeadquarter\": false}";
        mockMvc.perform(put("/v1/swift-codes/SWIFT123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Swift code updated successfully"));
    }

    @Test
    void testDeleteSwiftCode() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("SWIFT123");
        swiftCode.setBankName("Bank ABC");
        swiftCode.setCountryISO2("PL");
        swiftCode.setCountryName("PL");
        swiftCode.setAddress("");
        swiftCode.setHeadquarter(false);
        swiftCodeRepository.save(swiftCode);

        mockMvc.perform(delete("/v1/swift-codes/SWIFT123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Swift code deleted successfully"));

        Optional<SwiftCode> deletedSwiftCode = swiftCodeRepository.findBySwiftCode("SWIFT123");
        assert deletedSwiftCode.isEmpty();
    }

    @Test
    void testImportSwiftCodesFromExcel() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "swift-codes.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", createTestExcelFile()
        );

        mockMvc.perform(multipart("/v1/swift-codes/import").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("SWIFT codes imported successfully."));
    }

    private byte[] createTestExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("SWIFT Codes").createRow(0).createCell(0).setCellValue("swiftCode");
        workbook.getSheetAt(0).getRow(0).createCell(1).setCellValue("bankName");
        workbook.getSheetAt(0).getRow(0).createCell(2).setCellValue("country");
        workbook.getSheetAt(0).getRow(0).createCell(3).setCellValue("address");
        workbook.getSheetAt(0).createRow(1).createCell(0).setCellValue("SWIFT001");
        workbook.getSheetAt(0).getRow(1).createCell(1).setCellValue("Bank A");
        workbook.getSheetAt(0).getRow(1).createCell(2).setCellValue("US");
        workbook.getSheetAt(0).getRow(1).createCell(3).setCellValue("");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
