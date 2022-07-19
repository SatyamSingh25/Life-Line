package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //returns list of transaction details
    List<Transaction> findByUserEmail(String userEmail);

    Transaction findById(Long id);

//    Transaction findByTransactionID(Long id);
}
