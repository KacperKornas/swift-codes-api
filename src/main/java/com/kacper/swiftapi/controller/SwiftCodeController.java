package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.dto.CountrySwiftCodesDto;
import com.kacper.swiftapi.dto.MessageResponse;
import com.kacper.swiftapi.dto.SwiftCodeDto;
import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {
    @Autowired
    private SwiftCodeService service;
    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeDto> getSwiftCode(@PathVariable String swiftCode) {
        return ResponseEntity.ok(service.getSwiftCodeByCode(swiftCode));
    }
    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<CountrySwiftCodesDto> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        return ResponseEntity.ok(service.getSwiftCodesByCountry(countryISO2));
    }
    @PostMapping
    public ResponseEntity<MessageResponse> createSwiftCode(@RequestBody SwiftCode swiftCode) {
        String message = service.createSwiftCode(swiftCode);
        return ResponseEntity.status(201).body(new MessageResponse(message));
    }
    @PutMapping("/{swiftCode}")
    public ResponseEntity<MessageResponse> updateSwiftCode(@PathVariable String swiftCode, @RequestBody SwiftCode updatedSwiftCode) {
        String message = service.updateSwiftCode(swiftCode, updatedSwiftCode);
        return ResponseEntity.ok(new MessageResponse(message));
    }
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponse> deleteSwiftCode(@PathVariable String swiftCode) {
        String message = service.deleteSwiftCode(swiftCode);
        return ResponseEntity.ok(new MessageResponse(message));
    }
    @PostMapping("/import")
    public ResponseEntity<MessageResponse> importSwiftCodes(@RequestParam("file") MultipartFile file) {
        try {
            service.importSwiftCodesFromExcel(file);
            return ResponseEntity.ok(new MessageResponse("SWIFT codes imported successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Failed to import SWIFT codes."));
        }
    }
}
