package com.banking.banking_api.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotBlank(message = "Password is required")
    private String password;
}
