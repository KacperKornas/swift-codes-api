package com.kacper.swiftapi.controller;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

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
                .orElseThrow(() -> new SwiftCodeNotFoundException("Swift code with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<SwiftCode> createSwiftCode(@RequestBody SwiftCode swiftCode) {
        SwiftCode createdSwiftCode = service.createSwiftCode(swiftCode);
        return ResponseEntity.status(201).body(createdSwiftCode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SwiftCode> updateSwiftCode(@PathVariable Long id, @RequestBody SwiftCode swiftCode) {
        SwiftCode updated = service.updateSwiftCode(id, swiftCode);
        return ResponseEntity.ok(updated);
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
            throw new SwiftCodeProcessingException("Failed to import SWIFT codes: " + e.getMessage());
        }
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(SwiftCodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SwiftCodeProcessingException.class)
    public ResponseEntity<String> handleProcessingException(SwiftCodeProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}

class SwiftCodeNotFoundException extends RuntimeException {
    public SwiftCodeNotFoundException(String message) {
        super(message);
    }
}

class SwiftCodeProcessingException extends RuntimeException {
    public SwiftCodeProcessingException(String message) {
        super(message);
    }
}
