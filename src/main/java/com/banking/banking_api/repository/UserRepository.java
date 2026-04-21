package com.banking.banking_api.repository;
import com.banking.banking_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

// This is an interface, not a class.
// Spring Data JPA reads the method names and generates the SQL automatically
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}

