package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.BloodBankRequest;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodBankService {

    @Autowired
    private BloodBankRepository bloodBankRepository;

    //Adding new Blood Bank into the DB
    public ResponseEntity<?> addNewBloodBank(@NotNull BloodBank bloodBank){
        BloodBank bloodBankResponse = bloodBankRepository.findByBloodBankNameAndContact(bloodBank.getBloodBankName(), bloodBank.getContact());
        if(bloodBankResponse != null){
            return new ResponseEntity<>("adding Blood Bank Already Exist", HttpStatus.BAD_REQUEST);
        }

        //storing blood bank name & its City in lower case
        bloodBank.setBloodBankName(bloodBank.getBloodBankName().toLowerCase());
        bloodBank.setCity(bloodBank.getCity().toLowerCase());
        bloodBank.setEmail(bloodBank.getEmail().toLowerCase());

        //saving and returning the blood bank info
        return new ResponseEntity<>(bloodBankRepository.save(bloodBank), HttpStatus.CREATED);
    }

    //deleting the Blood Bank
    public ResponseEntity<?> deleteBloodBank(@NotNull BloodBankRequest bloodBankRequest) {
        //find blood bank by name and contact number (composite key) because multiple bank with same name may exist
        BloodBank bloodBankResponse = bloodBankRepository.findByBloodBankNameAndContact(bloodBankRequest.getBloodBankName(), bloodBankRequest.getContact());
        if(bloodBankResponse == null){
            return new ResponseEntity<>("Requesting Blood Bank Not Found", HttpStatus.NOT_FOUND);
        }
        //if blood bank exist delete by id
        bloodBankRepository.deleteById(bloodBankResponse.getId());
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    //Updating the Blood Bank blood units
    public ResponseEntity<?> updateBloodBankInfo(@NotNull BloodBank bloodBank) {
        BloodBank bloodBankResponse = bloodBankRepository.findByBloodBankNameAndContact(bloodBank.getBloodBankName(), bloodBank.getContact());
        if(bloodBankResponse == null){
            //log & Exception
            return new ResponseEntity<>("Requesting Blood Bank Not Found", HttpStatus.NOT_FOUND);
        }

        if(bloodBank.getAbnegative() != null){
            bloodBankResponse.setAbnegative(bloodBank.getAbnegative());
        }
        if(bloodBank.getAbpositive() != null){
            bloodBankResponse.setAbpositive(bloodBank.getAbpositive());
        }
        if(bloodBank.getAnegative() != null){
            bloodBankResponse.setAnegative(bloodBank.getAnegative());
        }
        if(bloodBank.getApositive() != null){
            bloodBankResponse.setApositive(bloodBank.getApositive());
        }
        if(bloodBank.getOnegative() != null){
            bloodBankResponse.setOnegative(bloodBank.getOnegative());
        }
        if(bloodBank.getOpositive() != null){
            bloodBankResponse.setOpositive(bloodBank.getOpositive());
        }
        if(bloodBank.getBnegative() != null){
            bloodBankResponse.setBnegative(bloodBank.getBnegative());
        }
        if(bloodBank.getBpositive() != null){
            bloodBankResponse.setBpositive(bloodBank.getBpositive());
        }

        return new ResponseEntity<>(bloodBankRepository.save(bloodBankResponse), HttpStatus.ACCEPTED);
    }

    //returning all the Blood Bank List
    public ResponseEntity<List<BloodBank>> showAllBloodBanks() {
        return new ResponseEntity<>(bloodBankRepository.findAll(), HttpStatus.ACCEPTED);
    }

    //finding the all blood bank at particular location
    public ResponseEntity<?> findLocationBloodBanks(@NotNull String city) {
        city = city.toLowerCase();
        return new ResponseEntity<>(bloodBankRepository.findByCity(city), HttpStatus.ACCEPTED); //return a list of Blood bank which are at X location
    }

    //finding blood bank by Contact
    public ResponseEntity<?> findBloodBank(Long id) {
        BloodBank bloodBankResponse = bloodBankRepository.findById(id).get();
        if(bloodBankResponse == null){
            return new ResponseEntity<>("Requesting blood bank is not available", HttpStatus.OK);
        }
        return new ResponseEntity<>(bloodBankResponse, HttpStatus.ACCEPTED);
    }
}
