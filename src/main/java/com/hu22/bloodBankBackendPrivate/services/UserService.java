package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.*;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.Role;
import com.hu22.bloodBankBackendPrivate.entities.Transaction;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import com.hu22.bloodBankBackendPrivate.repositories.RoleRepository;
import com.hu22.bloodBankBackendPrivate.repositories.TransactionRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    public ResponseEntity<?> registerNewUser(@NotNull User user){

        //checking existing user as per email
        User emailUserResponse = userRepository.findByEmail(user.getEmail());
        if(emailUserResponse != null){
            LOGGER.info("" + user.getEmail() + " :this email user is already registered ");
            return new ResponseEntity<>("email already exist", HttpStatus.BAD_REQUEST);
        }

        //checking existing user as per phone number
        User phoneUserResponse = userRepository.findByPhone(user.getPhone());
        if(phoneUserResponse != null){
            LOGGER.info(""+user.getPhone() + " : this phone number already registered");
            return new ResponseEntity<>("Phone number already exist", HttpStatus.BAD_REQUEST);
        }

        //storing encoded password
        user.setPassword(getEncodePassword(user.getPassword()));

        //storing location of user in lower case
        user.setCity(user.getCity().toLowerCase());

        //default role is USER whenever a person is sign up
        Role GeneralRole = new Role();
        GeneralRole.setRoleName("USER");
        GeneralRole.setRoleDescription("USER role");
        //setting up the role (User)
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(GeneralRole);
        user.setRoles(userRoles);

        LOGGER.info("Saving the New User details into Database");
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    public void initRolesAndRoles(){
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role GeneralRole = new Role();
        GeneralRole.setRoleName("USER");
        GeneralRole.setRoleDescription("USER role");
        roleRepository.save(GeneralRole);

        Role DonorRole = new Role();
        DonorRole.setRoleName("DONOR");
        DonorRole.setRoleDescription("Donor role");
        roleRepository.save(DonorRole);

        User adminUser = new User();
        adminUser.setPassword(getEncodePassword("admin@pass"));
        adminUser.setFirstName("admin");
        adminUser.setSecondName("admin");
        adminUser.setDob("25-04-1998");
        adminUser.setEmail("admin@test.com");
        adminUser.setPhone("9898989898");
        adminUser.setCity("kanpur");
        adminUser.setBloodGroup("B+");

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        userRepository.save(adminUser);
        LOGGER.info("added Admin");

        User user = new User();
        user.setPassword(getEncodePassword("satyam@pass"));
        user.setFirstName("satyam");
        user.setSecondName("singh");
        user.setDob("25-04-2001");
        user.setEmail("satyamsingh72@deloitte.com");
        user.setPhone("9400578035");
        user.setCity("kanpur");
        user.setBloodGroup("O+");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(GeneralRole);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    //Encoding the password
    public String getEncodePassword(String password){
        return passwordEncoder.encode(password);
    }

    //adding DONOR Role to User
    public ResponseEntity<?> addRoleToUser(String email){

        User responseUser = userRepository.findById(email).get();
        if(responseUser == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Role responseRole = new Role();
        responseRole.setRoleName("DONOR");
        responseRole.setRoleDescription("USER specific access /user/**");

        responseUser.addRole(responseRole);
        LOGGER.info("DONOR Role added to " + email + " user");

        return new ResponseEntity<>(userRepository.save(responseUser), HttpStatus.ACCEPTED);
    }

    //Ordering the Blood and Making transactions
    public ResponseEntity<?> confirmOrderAndMakeTransaction(@NotNull String email, @NotNull OrderConfirmRequest orderConfirmRequest) {

        User responseUser = userRepository.findById(email).get(); //found the user

        BloodBank bloodBankReponse = bloodBankRepository.findByContact(orderConfirmRequest.getPhone()); //getting the blood bank
        if(bloodBankReponse == null){
            return new ResponseEntity<>("Wrong bank phone details", HttpStatus.NOT_FOUND);
        }

        //find the requested blood group
        String bloodGroup = orderConfirmRequest.getBloodGroup().toLowerCase();



        //getting the amount of blood group unit
        if(bloodGroup.equals("a+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getApositive() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getApositive() - orderConfirmRequest.getUnit();
                System.out.println(finalUnitRemain);
                bloodBankReponse.setApositive(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("A+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");



                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("A+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");

                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("ab+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAbpositive() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getAbpositive() - orderConfirmRequest.getUnit();
                bloodBankReponse.setAbpositive(finalUnitRemain); //update the unit of AB+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("AB+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("AB+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("o+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getOpositive() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getOpositive() - orderConfirmRequest.getUnit();
                bloodBankReponse.setOpositive(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("O+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("O+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("b+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getBpositive() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getBpositive() - orderConfirmRequest.getUnit();
                bloodBankReponse.setBpositive(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("B+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("B+");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B+");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("a-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAnegative() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getAnegative() - orderConfirmRequest.getUnit();
                System.out.println(finalUnitRemain);
                bloodBankReponse.setAnegative(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("A-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("A-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("ab-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAbnegative() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getAbnegative() - orderConfirmRequest.getUnit();
                bloodBankReponse.setAbnegative(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("AB-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("AB-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("o-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getOnegative() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getOnegative() - orderConfirmRequest.getUnit();
                bloodBankReponse.setOnegative(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("O-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("O-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        else if(bloodGroup.equals("b-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getBnegative() >= orderConfirmRequest.getUnit()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getBnegative() - orderConfirmRequest.getUnit();
                bloodBankReponse.setBnegative(finalUnitRemain); //update the unit of A+

                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("B-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email

                transaction.setTimeStamp(date);
                transaction.setStatus("Accepted");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //sending mail
                mailService.onlineOrder(email, transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.ACCEPTED);

            }else{
                //add entry in failed transaction in transaction table
                //entry into the transaction table
                Transaction transaction = new Transaction();
                transaction.setBloodGroup("B-");
                transaction.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                transaction.setTypeOfTransaction("Request");
                transaction.setUserEmail(email); //user email
                transaction.setTimeStamp(date);
                transaction.setStatus("failed");
                transaction.setBloodBankId(bloodBankReponse.getId());
                transaction.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());
                transaction.setPhone(responseUser.getPhone()+"");
                String s2 = transaction.getPhone();
                String s3 = String.valueOf(transaction.getTimeStamp().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t+=s3;
                transaction.setTransactionID(t);
                //adding transaction in transaction table and returning the receipt
                transactionRepository.save(transaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B-");
                receipt.setUnitOfBloodGroup(orderConfirmRequest.getUnit());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(email);
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(orderConfirmRequest.getBloodBankName());
                receipt.setUserName(responseUser.getFirstName()+" "+responseUser.getSecondName());

                return new ResponseEntity<>(receipt, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Wrong Blood Group name", HttpStatus.OK);
    }

    //Updating the user details
    public ResponseEntity<?> updateUserInfo(String email, UserUpdateInfo userUpdateInfo) {
        //fetching the email user
        User userResponse = userRepository.findByEmail(email);

        if(userResponse == null){
            return new ResponseEntity<>("Requesting User not found", HttpStatus.NOT_FOUND);
        }

        //request to update the email of existing user
        if(userUpdateInfo.getEmail()!= null && !email.equals(userUpdateInfo.getEmail())){
            return new ResponseEntity<>("email can't be change", HttpStatus.BAD_REQUEST);
        }

        //request to update the DOB of a user
        if(userUpdateInfo.getDob()!=null && !userResponse.getDob().equals(userUpdateInfo.getDob())){
            userResponse.setDob(userUpdateInfo.getDob().toLowerCase());
            LOGGER.info("User DOB updated");
        }

        //updating the user phone number
        if(userUpdateInfo.getPhone()!=null && !userResponse.getPhone().equals(userUpdateInfo.getPhone())){
            User phoneUserResponse = userRepository.findByPhone(userUpdateInfo.getPhone());

            if(phoneUserResponse == null){ //if emails are equals then both are referring to same object
                userResponse.setPhone(userUpdateInfo.getPhone().toLowerCase());

                //updating user data into transaction table
                List<Transaction> userTransactions = transactionRepository.findByUserEmail(email);
                for(Transaction transaction: userTransactions){
                    transaction.setPhone(userUpdateInfo.getPhone().toLowerCase());

                    transactionRepository.save(transaction);
                }

                LOGGER.info("Updated the user Phone Number");
            }
            else{
                LOGGER.info("Requesting Phone Number already registered");
                return new ResponseEntity<>("This Phone number alrady already registered to another user", HttpStatus.BAD_REQUEST);
            }
        }

        //updating the blood group only if Blood group of a perticular user is not available in the database
        if(userResponse.getBloodGroup() == null && userUpdateInfo.getBloodGroup()!=null){
            userResponse.setBloodGroup(userUpdateInfo.getBloodGroup().toUpperCase());
            LOGGER.info("added Blood Group to the User");
        }
        else if(userResponse.getBloodGroup() != null && userUpdateInfo.getBloodGroup() != null){
            LOGGER.info("Blood Group of the User already registered");
            return new ResponseEntity<>("User Blood Group already mentioned in the data base", HttpStatus.BAD_REQUEST);
        }

        //updating the First Name of User
        if(userUpdateInfo.getFirstName()!=null && !userResponse.getFirstName().equals(userUpdateInfo.getFirstName())){
            userResponse.setFirstName(userUpdateInfo.getFirstName().toLowerCase());
            LOGGER.info("User first name is Updated into -> " + userUpdateInfo.getFirstName().toLowerCase() + "");
        }

        //Updating the second name User
        if(userUpdateInfo.getSecondName()!=null && !userResponse.getSecondName().equals(userUpdateInfo.getSecondName())){
            userResponse.setSecondName(userUpdateInfo.getSecondName().toLowerCase());
            LOGGER.info("User second name is Updated into ->" + userUpdateInfo.getSecondName().toLowerCase() + "");
        }

        //updating user data into transaction table
        List<Transaction> userTransactions = transactionRepository.findByUserEmail(email);
        for(Transaction transaction: userTransactions){
            String userName = "";
            userName = userResponse.getFirstName() + " "  + userResponse.getSecondName();
            transaction.setUserName(userName.toLowerCase().trim());
            transactionRepository.save(transaction);
        }

        //Updating the City of the User
        if(!userResponse.getCity().equals(userUpdateInfo.getCity())){
            userResponse.setCity(userUpdateInfo.getCity().toLowerCase());
            LOGGER.info("User city updated into -> " + userUpdateInfo.getCity().toLowerCase() + "");
        }

        LOGGER.info("Saving the Updated Details of user into DB");
        //Saving the Updated details
        return new ResponseEntity<>(userRepository.save(userResponse), HttpStatus.ACCEPTED);
    }

    //remove DONOR role
    public ResponseEntity<?> removeRole(@NotNull String email){

        User responseUser = userRepository.findById(email).get();
        if(responseUser == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Set<Role> responseUserRoles = responseUser.getRoles();
        responseUserRoles.removeAll(responseUserRoles);

        Role responseRole = new Role();
        responseRole.setRoleName("USER");
        responseRole.setRoleDescription("USER role");

//        responseUser.addRole(responseRole);
        responseUserRoles.add(responseRole);
        responseUser.setRoles(responseUserRoles);

        LOGGER.info("removed the DONOR Role from " + email + "") ;
        return new ResponseEntity<>(userRepository.save(responseUser), HttpStatus.ACCEPTED);
    }


    //need Username user email complete transaction detail and also blood bank detail
    //first get transaction id then from transaction id get blood bank id from blood bank id get blood bank details
    public ResponseEntity<?> showReceipt(OnlyTokenRequest onlyTokenRequest,Long id) {

        Transaction transaction = transactionRepository.findById(id);

        if(transaction==null){
            return new ResponseEntity<>("Transaction with this id does not exist", HttpStatus.ACCEPTED);
        }

        BloodBank bloodBank = bloodBankRepository.findByid(transaction.getBloodBankId());
        if(bloodBank==null){
            return new ResponseEntity<>("Blood Bank with this id was deleted", HttpStatus.ACCEPTED);
        }

        ReceiptTransaction receiptTransaction = new ReceiptTransaction(transaction.getId(), transaction.getTransactionID(), transaction.getBloodGroup(), transaction.getUserName(),transaction.getUnitOfBloodGroup(),  transaction.getTypeOfTransaction(), transaction.getUserEmail(), transaction.getPhone(), transaction.getTimeStamp(), transaction.getStatus(), bloodBank.getId(), bloodBank.getBloodBankName(), bloodBank.getCity(), bloodBank.getContact(), bloodBank.getEmail());

        return new ResponseEntity<>(receiptTransaction, HttpStatus.ACCEPTED);

    }

    //Show User Specific Transaction receipt
    public ResponseEntity<?> showUserReceipt(String email,Long id) {

        Transaction transaction = transactionRepository.findById(id);

        if(transaction==null){
            return new ResponseEntity<>("id does not exist", HttpStatus.ACCEPTED);
        }

        if(!email.equals(transaction.getUserEmail())){
            return new ResponseEntity<>("user not allowed", HttpStatus.ACCEPTED);
        }

        BloodBank bloodBank = bloodBankRepository.findByid(transaction.getBloodBankId());

        if(bloodBank==null){
            return new ResponseEntity<>("Blood Bank with this id was deleted", HttpStatus.ACCEPTED);
        }

        ReceiptTransaction receiptTransaction = new ReceiptTransaction(
                transaction.getId(),
                transaction.getTransactionID(),
                transaction.getBloodGroup(),
                transaction.getUserName(),
                transaction.getUnitOfBloodGroup(),
                transaction.getTypeOfTransaction(),
                transaction.getUserEmail(),
                transaction.getPhone(),
                transaction.getTimeStamp(),
                transaction.getStatus(),
                bloodBank.getId(),
                bloodBank.getBloodBankName(),
                bloodBank.getCity(),
                bloodBank.getContact(),
                bloodBank.getEmail());

        return new ResponseEntity<>(receiptTransaction, HttpStatus.ACCEPTED);

    }
//
}
