package com.banking.banking_api.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = " name is mandatory")
    private String name;
    private String phoneNumber;
    private String address;
    private String countryCode;
}
