package com.banking.banking_api.repository;

import com.banking.banking_api.entity.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {
    Optional<OtpRecord> findByEmail(String email);

    // Delete the OTP record after successful password reset
    void deleteByEmail(String email);
}