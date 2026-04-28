package com.banking.banking_api.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.banking.banking_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.banking_api.dto.CheckPinResponse;
import com.banking.banking_api.dto.CreatePinResponse;
import com.banking.banking_api.dto.UpdatePinResponse;
import com.banking.banking_api.dto.PinRequest;
import com.banking.banking_api.dto.UpdatePinRequest;
import com.banking.banking_api.dto.TransactionRequest;
import com.banking.banking_api.dto.TransactionResponse;
import com.banking.banking_api.dto.TransferRequest;
import com.banking.banking_api.dto.TransactionHistoryResponse;
import com.banking.banking_api.dto.AccountProfileResponse;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/check-pin")
     public ResponseEntity<CheckPinResponse> checkPin() {
        CheckPinResponse response = accountService.checkPin();
        return ResponseEntity.ok(response);
}

    @PostMapping("/create-pin")
     public ResponseEntity<CreatePinResponse> createPin(@Valid @RequestBody PinRequest request) {
        CreatePinResponse response = accountService.createPin(request);
        return ResponseEntity.ok(response);
}

    @GetMapping("/update-pin")
     public ResponseEntity<UpdatePinResponse> updatePin(@Valid @RequestBody UpdatePinRequest request) {
        UpdatePinResponse response = accountService.updatePin(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid@RequestBody TransactionRequest request) {
        TransactionResponse response = accountService.deposit(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = accountService.withdraw(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/fund-transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = accountService.transfer(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/transaction-history")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory( ) {
        return ResponseEntity.ok(accountService.getTransactionHistory());
    }
    @GetMapping("/account-profile")
    public ResponseEntity<AccountProfileResponse> getAccountProfile() {
        return ResponseEntity.ok(accountService.getAccountProfile());
}
}