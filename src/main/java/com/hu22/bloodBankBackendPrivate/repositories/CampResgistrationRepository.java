package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.CampaignRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampResgistrationRepository extends JpaRepository<CampaignRegistration, Integer> {
    List<CampaignRegistration> findByUserEmail(String email);
    List<CampaignRegistration> findByBloodBankId(Long bloodBankId);
}
