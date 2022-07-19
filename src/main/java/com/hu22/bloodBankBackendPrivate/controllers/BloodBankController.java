package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.BloodBankRequest;
import com.hu22.bloodBankBackendPrivate.dto.LocationBloodRequest;
import com.hu22.bloodBankBackendPrivate.dto.OnlyTokenRequest;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.services.BloodBankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class BloodBankController {

    @Autowired
    private BloodBankService bloodBankService;

    //admin adding New Blood Bank
    @PostMapping("/admin/bloodBank")
    public ResponseEntity<?> addBloodBank(@RequestHeader(value="Authorization") OnlyTokenRequest jwtResponse, @RequestBody BloodBank bloodBank){
        return bloodBankService.addNewBloodBank(bloodBank);
    }

    //User can see all Blood Banks
    //returns list of bloodBanks
    @GetMapping("/bloodBanks")
    public ResponseEntity<List<BloodBank>> allBloodBanks(){
        return bloodBankService.showAllBloodBanks();
    }


    //User get is List of Banks in a specific city
    @GetMapping("/user/bloodBank/city/{city}")
    public ResponseEntity<?> locationBasedBloodBanks(@NotNull @PathVariable("city") String city){
        return bloodBankService.findLocationBloodBanks(city);
    }

    //specific Blood Bank by ID
    @GetMapping("/bloodBankInfo/id/{id}")
    public ResponseEntity<?> specificBloodBank(@NotNull @PathVariable("id") Long id){
        return bloodBankService.findBloodBank(id);
    }


}
