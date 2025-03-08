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
        SwiftCode swiftCode = new SwiftCode(null, "SWIFT123", "Bank ABC", "PL");
        swiftCodeRepository.save(swiftCode);

        mockMvc.perform(get("/api/swift-codes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("SWIFT123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bankName").value("Bank ABC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].country").value("PL"));
    }

    @Test
    void testCreateSwiftCode() throws Exception {
        mockMvc.perform(post("/api/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"SWIFT456\", \"bankName\": \"Bank XYZ\", \"country\": \"DE\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("SWIFT456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").value("Bank XYZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("DE"));
    }

    @Test
    void testUpdateSwiftCode() throws Exception {
        SwiftCode swiftCode = swiftCodeRepository.save(new SwiftCode(null, "SWIFT123", "Bank ABC", "PL"));

        mockMvc.perform(put("/api/swift-codes/" + swiftCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"SWIFT456\", \"bankName\": \"Bank XYZ\", \"country\": \"DE\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("SWIFT456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").value("Bank XYZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("DE"));
    }

    @Test
    void testDeleteSwiftCode() throws Exception {
        SwiftCode swiftCode = swiftCodeRepository.save(new SwiftCode(null, "SWIFT123", "Bank ABC", "PL"));

        mockMvc.perform(delete("/api/swift-codes/" + swiftCode.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Optional<SwiftCode> deletedSwiftCode = swiftCodeRepository.findById(swiftCode.getId());
        assert deletedSwiftCode.isEmpty();
    }

    @Test
    void testImportSwiftCodesFromExcel() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "swift-codes.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", createTestExcelFile()
        );

        mockMvc.perform(multipart("/api/swift-codes/import").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("SWIFT codes imported successfully."));
    }

    private byte[] createTestExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("SWIFT Codes").createRow(0).createCell(0).setCellValue("code");
        workbook.getSheetAt(0).getRow(0).createCell(1).setCellValue("bankName");
        workbook.getSheetAt(0).getRow(0).createCell(2).setCellValue("country");
        workbook.getSheetAt(0).createRow(1).createCell(0).setCellValue("SWIFT001");
        workbook.getSheetAt(0).getRow(1).createCell(1).setCellValue("Bank A");
        workbook.getSheetAt(0).getRow(1).createCell(2).setCellValue("US");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
