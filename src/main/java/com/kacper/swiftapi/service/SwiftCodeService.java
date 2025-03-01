package com.kacper.swiftapi.service;

import com.kacper.swiftapi.entity.SwiftCode;
import com.kacper.swiftapi.repository.SwiftCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwiftCodeService {

    @Autowired
    private SwiftCodeRepository repository;

    public List<SwiftCode> getAllSwiftCodes() {
        return repository.findAll();
    }

    public Optional<SwiftCode> getSwiftCodeById(Long id) {
        return repository.findById(id);
    }

    public Optional<SwiftCode> getSwiftCodeByCode(String code) {
        return repository.findByCode(code);
    }

    public SwiftCode createSwiftCode(SwiftCode swiftCode) {
        return repository.save(swiftCode);
    }

    public SwiftCode updateSwiftCode(Long id, SwiftCode updatedSwiftCode) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCode(updatedSwiftCode.getCode());
                    existing.setBankName(updatedSwiftCode.getBankName());
                    existing.setCountry(updatedSwiftCode.getCountry());
                    existing.setCity(updatedSwiftCode.getCity());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("SWIFT code not found with id: " + id));
    }

    public void deleteSwiftCode(Long id) {
        repository.deleteById(id);
    }
}
