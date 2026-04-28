package com.banking.banking_api.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class TransferRequest {
@NotBlank(message="Pin is required") 
private String pin;

@NotBlank(message="Recipient account number is required")
private String recipientAccountNumber;

@NotNull(message="Amount is required")
@DecimalMin(value = "0.01", message = "Amount must be greater than zero")
private BigDecimal amount;

}
