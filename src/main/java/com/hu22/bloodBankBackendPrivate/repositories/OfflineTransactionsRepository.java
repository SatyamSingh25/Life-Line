package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.OfflineTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineTransactionsRepository extends JpaRepository<OfflineTransaction, Long> {
}
