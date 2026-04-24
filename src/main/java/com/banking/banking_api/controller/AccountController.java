package com.banking.banking_api.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.banking.banking_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.banking_api.dto.CheckPinResponse;
import com.banking.banking_api.dto.CreatePinResponse;
import com.banking.banking_api.dto.UpdatePinResponse;
import com.banking.banking_api.dto.PinRequest;
import com.banking.banking_api.dto.UpdatePinRequest;

import jakarta.validation.Valid;

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

    @GetMapping("/create-pin")
     public ResponseEntity<CreatePinResponse> createPin(@Valid @RequestBody PinRequest request) {
        CreatePinResponse response = accountService.createPin(request);
        return ResponseEntity.ok(response);
}

    @GetMapping("/update-pin")
     public ResponseEntity<UpdatePinResponse> updatePin(@Valid @RequestBody UpdatePinRequest request) {
        UpdatePinResponse response = accountService.updatePin(request);
        return ResponseEntity.ok(response);
}
}
