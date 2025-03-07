package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.service.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SwiftCodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SwiftCodeService swiftCodeService;

    @InjectMocks
    private SwiftCodeController swiftCodeController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(swiftCodeController).build();
    }

    @Test
    void testGetAllSwiftCodes() throws Exception {
        SwiftCode swiftCode = new SwiftCode(null, "SWIFT123", "Bank ABC", "PL");
        when(swiftCodeService.getAllSwiftCodes()).thenReturn(List.of(swiftCode));

        mockMvc.perform(get("/api/swift-codes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SWIFT123"))
                .andExpect(jsonPath("$[0].bankName").value("Bank ABC"))
                .andExpect(jsonPath("$[0].country").value("PL"));
    }

    @Test
    void testGetAllSwiftCodes_EmptyList() throws Exception {
        when(swiftCodeService.getAllSwiftCodes()).thenReturn(List.of());

        mockMvc.perform(get("/api/swift-codes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetSwiftCodeById() throws Exception {
        SwiftCode swiftCode = new SwiftCode(null, "SWIFT123", "Bank ABC", "PL");
        when(swiftCodeService.getSwiftCodeById(1L)).thenReturn(Optional.of(swiftCode));

        mockMvc.perform(get("/api/swift-codes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SWIFT123"))
                .andExpect(jsonPath("$.bankName").value("Bank ABC"))
                .andExpect(jsonPath("$.country").value("PL"));
    }

    @Test
    void testGetSwiftCodeById_NotFound() throws Exception {
        when(swiftCodeService.getSwiftCodeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/swift-codes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSwiftCode() throws Exception {
        SwiftCode swiftCode = new SwiftCode(null, "SWIFT123", "Bank ABC", "PL");
        when(swiftCodeService.createSwiftCode(any(SwiftCode.class))).thenReturn(swiftCode);

        mockMvc.perform(post("/api/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"SWIFT123\", \"bankName\": \"Bank ABC\", \"country\": \"PL\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SWIFT123"))
                .andExpect(jsonPath("$.bankName").value("Bank ABC"))
                .andExpect(jsonPath("$.country").value("PL"));
    }

    @Test
    void testDeleteSwiftCode() throws Exception {
        doNothing().when(swiftCodeService).deleteSwiftCode(1L);

        mockMvc.perform(delete("/api/swift-codes/1"))
                .andExpect(status().isNoContent());
    }
}
