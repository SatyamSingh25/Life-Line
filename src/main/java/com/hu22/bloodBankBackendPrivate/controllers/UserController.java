package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.*;
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

import javax.annotation.PostConstruct;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodBankService bloodBankService;

    @Autowired
    private TransactionService transactionService;


    //this endpoint registers new user and checks if user already exist or not in userService
    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerNewUser(@RequestBody User user){
        return userService.registerNewUser(user);
    }

    //?
    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRolesAndRoles();
    }

    // return user details
    @GetMapping("/user/profile")
    public User showUserProfile(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest) {

        String fullJwt = onlyTokenRequest.getToken();
        String jwt = fullJwt.substring(7,fullJwt.length());
        JwtUtil jwtUtil = new JwtUtil();
        String email = jwtUtil.getEmailFromToken(jwt);
        User response = userRepository.findById(email).get();


        return response;
    }

    @GetMapping("/user/addRole") //adding the DONOR
    public ResponseEntity<?> addRoleToUser(@RequestHeader(value="Authorization") OnlyTokenRequest jwtResponse){
        String email = tokenToEmail(jwtResponse);
        return userService.addRoleToUser(email);
    }

    //remove donor role from user
    @GetMapping("/user/removeRole") //removing the DONOR
    public ResponseEntity<?> removeRole(@RequestHeader(value="Authorization") OnlyTokenRequest jwtResponse){
        String email = tokenToEmail(jwtResponse);
        return userService.removeRole(email);
    }

    //list all donors
    @GetMapping("/donors")
    public List<User> listOfDonors(){
        String role = "DONOR";
        return userRepository.findByRolesRoleName(role);
    }

    @PostMapping("/user/receipt")
    public ResponseEntity<?> orderForBlood(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest, @RequestBody OrderConfirmRequest orderConfirmRequest){
       //it should return the JSON for receipt
       //perform the insertion in transaction table
        String email = tokenToEmail(onlyTokenRequest);
        return userService.confirmOrderAndMakeTransaction(email, orderConfirmRequest);
    }

    @GetMapping("/user/transactions")
    public ResponseEntity<?> userTransactions(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest){
        String email = tokenToEmail(onlyTokenRequest);
        return transactionService.userAllTransactions(email);
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> userUpdate(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest, @RequestBody UserUpdateInfo userUpdateInfo){
        String email = tokenToEmail(onlyTokenRequest);
        return userService.updateUserInfo(email, userUpdateInfo);
    }

    @GetMapping("/user/showReceipt/{id}")
    public ResponseEntity<?> showUserReceipt(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest,@NotNull @PathVariable("id") Long id){
        String email = tokenToEmail(onlyTokenRequest);
        return userService.showUserReceipt(email,id);
    }

    @DeleteMapping("/abrakadabradeletetransactions")
    public String deletetransaction(){
        return transactionService.deleteTransactions();
    }

    public String tokenToEmail(OnlyTokenRequest onlyTokenRequest){
        String fullJwt = onlyTokenRequest.getToken();
        String jwt = fullJwt.substring(7,fullJwt.length());
        JwtUtil jwtUtil = new JwtUtil();
        String email = jwtUtil.getEmailFromToken(jwt);
        return email;
    }//
}


























