package com.banking.banking_api.service;

import com.banking.banking_api.dto.PinRequest;
import com.banking.banking_api.dto.UpdatePinRequest;
import com.banking.banking_api.entity.Account;
import com.banking.banking_api.entity.User;
import com.banking.banking_api.exception.AppExceptions;
import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import com.banking.banking_api.dto.CheckPinResponse;
import com.banking.banking_api.dto.CreatePinResponse;
import com.banking.banking_api.dto.TransactionResponse;
import com.banking.banking_api.dto.UpdatePinResponse;
import com.banking.banking_api.dto.TransactionRequest;
import com.banking.banking_api.entity.Transaction;
import com.banking.banking_api.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;

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

    @Transactional
    public CreatePinResponse createPin(PinRequest pinRequest){
        Account account = getCurrentAccount();
        if (account.isHasPin()){
            throw new AppExceptions.UnauthorizedException("PIN already exists for this account");
        }
        if(!passwordEncoder.matches(pinRequest.getPassword(),account.getUser().getPassword())){
            throw new AppExceptions.UnauthorizedException("Incorrect password");
        }
        account.setPinHash(passwordEncoder.encode(pinRequest.getPin()));
        account.setHasPin(true);
        accountRepository.save(account);
        log .info("PIN created for account: {}", account.getAccountNumber());
        return CreatePinResponse.builder()
                .hasPin(true)
                .message("PIN created successfully")
                .build();

    }

    @Transactional
    public UpdatePinResponse updatePin(UpdatePinRequest request){
        Account account = getCurrentAccount();
        if (!account.isHasPin()){
            throw new AppExceptions.UnauthorizedException("No existing PIN found for this account");
        }
        if(!passwordEncoder.matches(request.getPassword(),account.getUser().getPassword())){
            throw new AppExceptions.UnauthorizedException("Incorrect current password");
        }
        if(!passwordEncoder.matches(request.getOldPin(),account.getPinHash())){
            throw new AppExceptions.UnauthorizedException("Incorrect current PIN");
        }
        account.setPinHash(passwordEncoder.encode(request.getNewPin()));
        accountRepository.save(account);
        log.info("PIN updated for account: {}", account.getAccountNumber());
        return UpdatePinResponse.builder()
                .hasPin(true)
                .message("PIN updated successfully")
                .build();

    }

    @Transactional
    public TransactionResponse deposit(TransactionRequest transactionRequest){
        Account account = getCurrentAccount();
        if (!account.isHasPin()){
            throw new AppExceptions.UnauthorizedException("No existing PIN found for this account");
        }
        if(!passwordEncoder.matches(transactionRequest.getPin(),account.getPinHash())){
            throw new AppExceptions.UnauthorizedException("PIN is not correct");
        } 
        // Add the deposit amount to the current balance
        // We use BigDecimal.add() — never use + operator with BigDecimal
        BigDecimal newBalance = account.getBalance().add(transactionRequest.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        // Create a new transaction record
        Transaction transation = Transaction.builder()
                .account(account)
                .amount(transactionRequest.getAmount())
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .balanceAfter(newBalance)
                .build();
        transactionRepository.save(transation);
        log.info("Deposit of {} successful for account: {}", transactionRequest.getAmount(), account.getAccountNumber());
        return TransactionResponse.builder()
                .message("Deposit successful")
                .balance(newBalance.toString())
                .build();
}

    @Transactional
    public TransactionResponse withdraw(TransactionRequest transactionRequest){
            Account account = getCurrentAccount();
            if (!account.isHasPin()){
                throw new AppExceptions.UnauthorizedException("No existing PIN found for this account");
            }
            if(!passwordEncoder.matches(transactionRequest.getPin(),account.getPinHash())){
                throw new AppExceptions.UnauthorizedException("PIN is not correct");
            } 
            if (account.getBalance().compareTo(transactionRequest.getAmount()) < 0){
                throw new AppExceptions.InsufficientBalanceException("Insufficient balance");
            }
            // Subtract the withdrawal amount from the current balance
            BigDecimal newBalance = account.getBalance().subtract(transactionRequest.getAmount());
            account.setBalance(newBalance);
            accountRepository.save(account);
            // Create a new transaction record
            Transaction transation = Transaction.builder()
                    .account(account)
                    .amount(transactionRequest.getAmount())
                    .transactionType(Transaction.TransactionType.WITHDRAWAL)
                    .balanceAfter(newBalance)
                    .build();
            transactionRepository.save(transation);
            log.info("Withdrawal of {} successful for account: {}", transactionRequest.getAmount(), account.getAccountNumber());
            return TransactionResponse.builder()
                    .message("Withdrawal successful")
                    .balance(newBalance.toString())
                    .build();
    }
}