package com.banking.banking_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPinResponse {
    private String message;
    private boolean hasPin;
}
