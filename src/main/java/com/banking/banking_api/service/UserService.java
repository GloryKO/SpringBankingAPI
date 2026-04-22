package com.banking.banking_api.service;
import com.banking.banking_api.dto.RegisterRequest;
import com.banking.banking_api.dto.RegisterResponse;
import com.banking.banking_api.entity.Account;
import com.banking.banking_api.entity.User;
import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Lombok: generates constructor for all final fields (dependency injection)
@Slf4j // Lombok annotation to generate a logger
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){  //email cant be null because of @NotBlank in DTO, must also be unique
            throw new RuntimeException("Email already in use");
        }
        if (request.getPhoneNumber() != null && userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new RuntimeException("Phone number already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) //hash the password before saving
                .countryCode(request.getCountryCode())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
        User savedUser = userRepository.save(user);
        log.info("User registered with email: {}", savedUser.getEmail());
        String accountNumber = generateUniqueAccountNumber();
        Account account = Account.builder()
                .user(savedUser)
                .accountNumber(accountNumber)
                .build();
        Account savedAccount = accountRepository.save(account);
        log.info("Account created for user: {}", savedUser.getEmail());

        return RegisterResponse.builder()
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .countryCode(savedUser.getCountryCode())
                .phoneNumber(savedUser.getPhoneNumber())
                .address(savedUser.getAddress())
                .accountNumber(savedAccount.getAccountNumber())
                .ifscCode(savedAccount.getIfscCode())
                .branch(savedAccount.getBranch())
                .accountType(savedAccount.getAccountType())
                .build();
    }
    private String generateUniqueAccountNumber(){
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.valueOf(100000+random.nextInt(900000)); // generates a random 6-digit number
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
        }
}
