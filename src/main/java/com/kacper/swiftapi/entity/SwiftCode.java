package com.kacper.swiftapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
@Entity
public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code", nullable = false, unique = true)
    private String swiftCode;
    @Column(name = "bank_name", nullable = false)
    private String bankName;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "countryiso2", nullable = false)
    private String countryISO2;
    @Column(name = "country_name", nullable = false)
    private String countryName;
    @Column(name = "is_headquarter", nullable = false)
    private boolean isHeadquarter;
    public SwiftCode() {}
    public SwiftCode(Long id, String swiftCode, String bankName, String address, String countryISO2, String countryName, boolean isHeadquarter) {
        this.id = id;
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.address = address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSwiftCode() { return swiftCode; }
    public void setSwiftCode(String swiftCode) { this.swiftCode = swiftCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCountryISO2() { return countryISO2; }
    public void setCountryISO2(String countryISO2) { this.countryISO2 = countryISO2; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public boolean isHeadquarter() { return isHeadquarter; }
    public void setHeadquarter(boolean headquarter) { isHeadquarter = headquarter; }
}
