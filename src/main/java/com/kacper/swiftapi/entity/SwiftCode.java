package com.kacper.swiftapi.entity;

import jakarta.persistence.*;

@Entity
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String code;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String country;

    // Konstruktor domyślny
    public SwiftCode() {
    }

    // Konstruktor z parametrami
    public SwiftCode(Long id, String code, String bankName, String country) {
        this.id = id;
        this.code = code;
        this.bankName = bankName;
        this.country = country;
    }

    // Getter i Setter dla 'id'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter i Setter dla 'code'
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getter i Setter dla 'bankName'
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    // Getter i Setter dla 'country'
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
