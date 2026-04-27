package com.banking.banking_api.dto;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

// We reuse this same DTO for both deposit and withdrawal
// since both need an amount and a PIN
@Data
@Builder
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal amount;

    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    private String pin;
}