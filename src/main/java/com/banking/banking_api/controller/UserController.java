package com.banking.banking_api.controller;
import com.banking.banking_api.dto.LoginRequest;
import com.banking.banking_api.dto.RegisterRequest;
import com.banking.banking_api.dto.RegisterResponse;
import com.banking.banking_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){ // @Valid triggers validation based on annotations in RegisterRequest @RequestBody binds the incoming JSON to the RegisterRequest object
        RegisterResponse response = userService.register(request);
        return ResponseEntity.ok(response); 
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {  
    return ResponseEntity.ok(userService.login(request));
}
}
