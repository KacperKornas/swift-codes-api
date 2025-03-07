package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.repository.SwiftCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

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
                        .contentType("application/json")
                        .content("{\"code\": \"SWIFT123\", \"bankName\": \"Bank ABC\", \"country\": \"PL\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("SWIFT123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").value("Bank ABC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("PL"));
    }
}
