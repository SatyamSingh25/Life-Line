package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.OfflinePurchaseRequest;
import com.hu22.bloodBankBackendPrivate.dto.ReceiptResponse;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.OfflineTransaction;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import com.hu22.bloodBankBackendPrivate.repositories.OfflineTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OfflineTransactionService {

    @Autowired
    private OfflineTransactionsRepository offlineTransactionsRepository;

    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private MailService mailService;

    //Offline purchase by admin
    public ResponseEntity<?> offlinePurchase(OfflinePurchaseRequest offlinePurchaseRequest) {

        BloodBank bloodBankReponse = bloodBankRepository.findById(offlinePurchaseRequest.getBloodBankId()).get();

        //find the requested blood Group
        String bloodGroup = offlinePurchaseRequest.getBloodGroup().toLowerCase();

        if(bloodGroup.equals("a+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getApositive() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getApositive() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setApositive(finalUnitRemain); //update the unit of A+

                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("A+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());


                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed offline transaction
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("A+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique transaction ID
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("ab+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAbpositive() >= offlinePurchaseRequest.getUnitOfBlood()){
                //offline transaction possible
                Long finalUnitRemain = bloodBankReponse.getAbpositive() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setAbpositive(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("AB+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed offline transaction
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("AB+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique transaction ID
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("o+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getOpositive() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getOpositive() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setOpositive(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("O+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed transaction
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("O+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("b+")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getBpositive() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getBpositive() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setBpositive(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("B+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed B+ transaction
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("B+");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B+");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("a-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAnegative() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getAnegative() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setAnegative(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("A-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed Transaction with A-
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("A-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
//                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("A-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("ab-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getAbnegative() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getAbnegative() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setAbnegative(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("AB-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //Failed Transaction of AB-
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("AB-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
//                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("AB-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("o-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getOnegative() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getOnegative() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setOnegative(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("O-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed Offline transaction of O-
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("O-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
//                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("O-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }
        else if(bloodGroup.equals("b-")){
            long millis=System.currentTimeMillis();
            Date date=new Date(millis);
            if(bloodBankReponse.getBnegative() >= offlinePurchaseRequest.getUnitOfBlood()){
                //transaction possible
                Long finalUnitRemain = bloodBankReponse.getBnegative() - offlinePurchaseRequest.getUnitOfBlood();
                bloodBankReponse.setBnegative(finalUnitRemain);

                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("B-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Accepted");
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //sending mail
                mailService.orderConfirmMail(offlinePurchaseRequest.getUserEmail(), offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Accepted");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
            else{
                //failed Offline Transaction of B-
                //entry into offline Transaction table
                OfflineTransaction offlineTransaction = new OfflineTransaction();
                offlineTransaction.setBloodGroup("B-");
                offlineTransaction.setUnitOfBlood(offlinePurchaseRequest.getUnitOfBlood());

                offlineTransaction.setDate(date);
                offlineTransaction.setStatus("Failed");
//                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setBloodBankId(offlinePurchaseRequest.getBloodBankId());
                offlineTransaction.setUserAddress(offlinePurchaseRequest.getUserAdress().toUpperCase());
                offlineTransaction.setUserFullName(offlinePurchaseRequest.getUserFullName());
                offlineTransaction.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                offlineTransaction.setUserPhone(offlinePurchaseRequest.getUserPhone());

                //generating unique ID for transaction
                String s2 = offlineTransaction.getUserPhone();
                String s3 = String.valueOf(offlineTransaction.getDate().getTime());
                String t = "";
                for (int i = 0; i < 5; ++i) {
                    char ch = s2.charAt(i);
                    char ch2 = s3.charAt(6-i);
                    t =t+ch+ch2;
                }
                t += s3;
                offlineTransaction.setReceiptId(t);
                //adding offline transaction
                offlineTransactionsRepository.save(offlineTransaction);

                //creating a receipt
                ReceiptResponse receipt = new ReceiptResponse();
                receipt.setBloodGroup("B-");
                receipt.setUnitOfBloodGroup(offlinePurchaseRequest.getUnitOfBlood());
                receipt.setTypeOfTransaction("Request");
                receipt.setUserEmail(offlinePurchaseRequest.getUserEmail().toLowerCase());
                receipt.setTimeStamp(date);
                receipt.setStatus("Failed");
                receipt.setTimeStamp(date);
                receipt.setBloodBankName(bloodBankReponse.getBloodBankName());
                receipt.setUserName(offlinePurchaseRequest.getUserFullName());
                return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
            }
        }

        return new ResponseEntity<>("Wrong Blood Group name", HttpStatus.OK);
    }

    //returning list of all transaction
    public ResponseEntity<?> allOfflineTransactions() {
        return new ResponseEntity<>(offlineTransactionsRepository.findAll(), HttpStatus.OK);
    }

    //return single transaction
    public ResponseEntity<?> getSingleOfflineTransaction(Long id) {
        OfflineTransaction offlineTransaction = offlineTransactionsRepository.findById(id).get();
        if(offlineTransaction == null){
            return new ResponseEntity<>("Transaction not found", HttpStatus.OK);
        }
        return new ResponseEntity<>(offlineTransaction, HttpStatus.ACCEPTED);
    }
}
