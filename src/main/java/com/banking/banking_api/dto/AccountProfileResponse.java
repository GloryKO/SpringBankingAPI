package com.banking.banking_api.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import com.banking.banking_api.entity.Account;

@Data
@Builder
public class AccountProfileResponse { //uses both the account and user fields, so we can't reuse AccountResponse
    
    // Account fields
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;
    private String branch;
    private String ifscCode;
    private boolean hasPin;

    // User fields
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String countryCode;

    public static AccountProfileResponse fromEntities(Account account) {
        return AccountProfileResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .branch(account.getBranch())
                .ifscCode(account.getIfscCode())
                .hasPin(account.isHasPin())
                .name(account.getUser().getName())
                .email(account.getUser().getEmail())
                .phoneNumber(account.getUser().getPhoneNumber())
                .address(account.getUser().getAddress())
                .countryCode(account.getUser().getCountryCode())
                .build();
    }
}
