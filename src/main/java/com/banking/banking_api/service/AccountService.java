package com.banking.banking_api.service;

import com.banking.banking_api.dto.PinRequest;
import com.banking.banking_api.dto.UpdatePinRequest;
import com.banking.banking_api.entity.Account;
import com.banking.banking_api.entity.User;
import com.banking.banking_api.exception.AppExceptions;
import com.banking.banking_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import com.banking.banking_api.dto.CheckPinResponse;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    //helper method to get account number from logged in user
    private Account getCurrentAccount(){
        String accountNumber = SecurityContextHolder.getContext().getAuthentication().getName(); //account number is stored as the "username" in the token
        return accountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public CheckPinResponse checkPin() {
        Account account = getCurrentAccount();

        return CheckPinResponse.builder()
                .hasPin(account.isHasPin())
                .message(account.isHasPin() ? "PIN has been created for this account" : "PIN has not been created for this account")
                .build();
    }
}
