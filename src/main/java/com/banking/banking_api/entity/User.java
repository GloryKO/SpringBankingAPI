package com.banking.banking_api.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Data
@Builder //lmobok : generates getters, setters, equals 
@AllArgsConstructor //lombok: generates constructor with all fields 
@NoArgsConstructor //lombok : generates empty constructor which is required by JPA
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; //BCrypt hashed password

    @Column(name="country_code")
    private String countryCode;

    @Column(name="phone_number", unique = true)
    private String phoneNumber;

    private String address; //use default since it can be null and not unique so no need for @Column annotation

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist //JPA annotation to specify a method that should be called before the entity is persisted (saved) to the database
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
