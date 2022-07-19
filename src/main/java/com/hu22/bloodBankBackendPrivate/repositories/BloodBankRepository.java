package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {


    BloodBank findByBloodBankNameAndContact(String bloodBankName, String contact);
    BloodBank findByContact(String contact);


    List<BloodBank> findByCity(String city);

    BloodBank findByid(Long id);
}
