package com.kacper.swiftapi.repository;

import com.kacper.swiftapi.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    Optional<SwiftCode> findBySwiftCode(String swiftCode);
    List<SwiftCode> findByCountryISO2(String countryISO2);
    List<SwiftCode> findBySwiftCodeStartingWithAndIsHeadquarterFalse(String prefix);
}
