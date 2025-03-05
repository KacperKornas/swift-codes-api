package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/swift-codes")
public class SwiftCodeController {

    @Autowired
    private SwiftCodeService service;

    @GetMapping
    public List<SwiftCode> getAllSwiftCodes() {
        return service.getAllSwiftCodes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SwiftCode> getSwiftCodeById(@PathVariable Long id) {
        return service.getSwiftCodeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SwiftCode createSwiftCode(@RequestBody SwiftCode swiftCode) {
        return service.createSwiftCode(swiftCode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SwiftCode> updateSwiftCode(@PathVariable Long id, @RequestBody SwiftCode swiftCode) {
        try {
            SwiftCode updated = service.updateSwiftCode(id, swiftCode);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSwiftCode(@PathVariable Long id) {
        service.deleteSwiftCode(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<String> importSwiftCodesFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            service.importSwiftCodesFromExcel(file);
            return ResponseEntity.ok("SWIFT codes imported successfully.");
        } catch (IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            return ResponseEntity.status(500).body("Failed to import SWIFT codes: " + e.getMessage());
        }
    }
}
