package com.banking.banking_api.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String name;
    private String email;
    private String countryCode;
    private String phoneNumber;
    private String address;
    private String accountNumber;
    private String ifscCode;
    private String branch;
    private String accountType;
}
