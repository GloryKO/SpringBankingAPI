package com.banking.banking_api.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
    private String message;
    private String balance;
}
