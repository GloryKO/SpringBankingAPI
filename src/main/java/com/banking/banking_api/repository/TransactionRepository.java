package com.banking.banking_api.repository;
import org.springframework.stereotype.Repository;
import com.banking.banking_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.banking.banking_api.entity.Account;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);
}
