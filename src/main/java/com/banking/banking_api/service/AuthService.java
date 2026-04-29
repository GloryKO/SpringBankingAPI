package com.banking.banking_api.service;

import com.banking.banking_api.dto.ResetPasswordRequest;
import com.banking.banking_api.dto.SendOtpRequest;
import com.banking.banking_api.entity.OtpRecord;
import com.banking.banking_api.entity.User;
import com.banking.banking_api.exception.AppExceptions;
import com.banking.banking_api.repository.OtpRecordRepository;
import com.banking.banking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRecordRepository otpRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Transactional
    public Map<String, String> sendOtp(SendOtpRequest request) {

        // Step 1: verify the email belongs to a registered user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException(
                        "No account found with this email"));

        // Step 2: if an OTP already exists for this email, delete it
        // This handles the case where a user requests OTP multiple times
        // We always replace with a fresh one
        otpRecordRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> otpRecordRepository.delete(existing));

        // Step 3: generate a random 6 digit OTP
        String otp = generateOtp();

        // Step 4: store the OTP with a 5 minute expiry
        OtpRecord otpRecord = OtpRecord.builder()
                .email(request.getEmail())
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        otpRecordRepository.save(otpRecord);

        // Step 5: send the OTP to the user's email
        sendOtpEmail(user.getName(), request.getEmail(), otp);

        log.info("OTP sent to email: {}", request.getEmail());
        return Map.of("message", "OTP sent to your email");
    }

    // ─────────────────────────────────────────────────────────────────
    // RESET PASSWORD
    // ─────────────────────────────────────────────────────────────────
    @Transactional
    public Map<String, String> resetPassword(ResetPasswordRequest request) {

        // Step 1: find the OTP record for this email
        OtpRecord otpRecord = otpRecordRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException(
                        "No OTP found for this email. Please request a new one"));

        // Step 2: check the OTP has not expired
        // LocalDateTime.now().isAfter(expiresAt) means the expiry time has passed
        if (LocalDateTime.now().isAfter(otpRecord.getExpiresAt())) {
            // Delete the expired OTP so the user must request a fresh one
            otpRecordRepository.delete(otpRecord); //use delete since we have the object queried already
            throw new AppExceptions.InvalidOtpException(
                    "OTP has expired. Please request a new one");
        }

        // Step 3: check the OTP matches
        if (!otpRecord.getOtp().equals(request.getOtp())) {
            throw new AppExceptions.InvalidOtpException("Invalid OTP");
        }

        // Step 4: find the user and update their password
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException(
                        "No account found with this email"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Step 5: delete the OTP — it has been used and must never work again
        otpRecordRepository.delete(otpRecord);

        log.info("Password reset successful for email: {}", request.getEmail());
        return Map.of("message", "Password reset successful");
    }

    // ─────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────

    // Generates a random 6 digit number as a String
    // e.g. "048291", "739201"
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    private void sendOtpEmail(String name, String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Password Reset OTP");
        message.setText(
                "Hello " + name + ",\n\n" +
                "Your OTP for password reset is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Banking App"
        );
        mailSender.send(message);
    }
}