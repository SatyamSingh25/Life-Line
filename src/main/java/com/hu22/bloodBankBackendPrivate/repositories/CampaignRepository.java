package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByCityAndIsDeleted(String city, boolean isDeleted);
    List<Campaign> findByIsDeleted(boolean Deleted);
}
