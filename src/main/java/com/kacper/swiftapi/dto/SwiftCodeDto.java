package com.kacper.swiftapi.dto;

import java.util.List;
public class SwiftCodeDto {
    private String swiftCode;
    private String bankName;
    private String address;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private List<SwiftCodeDto> branches;
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
    public List<SwiftCodeDto> getBranches() { return branches; }
    public void setBranches(List<SwiftCodeDto> branches) { this.branches = branches; }
}
