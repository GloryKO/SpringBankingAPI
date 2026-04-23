package com.banking.banking_api.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UpdatePinRequest {
    @NotBlank
    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    private String oldPin;

    @NotBlank
    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    private String newPin;

    @NotBlank(message="Password is required")
    private String password;
}
