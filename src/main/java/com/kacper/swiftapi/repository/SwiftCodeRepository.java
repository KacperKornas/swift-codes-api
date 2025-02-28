package com.kacper.swiftapi.repository;

import com.kacper.swiftapi.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    Optional<SwiftCode> findByCode(String code);
}
