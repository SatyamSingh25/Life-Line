package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.BloodBankRequest;
import com.hu22.bloodBankBackendPrivate.dto.OnlyTokenRequest;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.services.BloodBankService;
import com.hu22.bloodBankBackendPrivate.services.TransactionService;
import com.hu22.bloodBankBackendPrivate.services.UserService;
import com.hu22.bloodBankBackendPrivate.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AdminController {

    @Autowired
    private BloodBankService bloodBankService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;


    //admin endpoint to delete a particular blood bank
    //need to pass jwt as header to validate endpoint
    //passing dto BloodBankRequest object as body
    @DeleteMapping("/admin/deleteBloodBank")
    public ResponseEntity<?> deleteBloodBank(@RequestHeader(value="Authorization") OnlyTokenRequest jwtResponse, @RequestBody BloodBankRequest bloodBankRequest){
        return bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    //endpoint to update BloodBank details
    //passing bloodBank entity object as body
    @PutMapping("/admin/updateBloodBank")
    public ResponseEntity<?> updateBloodBank(@RequestHeader(value="Authorization") OnlyTokenRequest jwtResponse, @RequestBody BloodBank bloodBank){
        return bloodBankService.updateBloodBankInfo(bloodBank);
    }

    // return response entity as all transactions done
    @GetMapping("/admin/transactions")
    public ResponseEntity<?> allTransactions(){
        return transactionService.allTransactions();
    }

    // admin profile
    @GetMapping("/admin/profile")
    public User showUserProfile(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest) {

        String fullJwt = onlyTokenRequest.getToken();
        String jwt = fullJwt.substring(7,fullJwt.length());
        JwtUtil jwtUtil = new JwtUtil();
        String email = jwtUtil.getEmailFromToken(jwt);
        User response = userRepository.findById(email).get();

        return response;
    }

    @GetMapping("/admin/showReceipt/{id}")
    public ResponseEntity<?> showReceipt(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest,@NotNull @PathVariable("id") Long id){
        return userService.showReceipt(onlyTokenRequest,id);
    }

}
