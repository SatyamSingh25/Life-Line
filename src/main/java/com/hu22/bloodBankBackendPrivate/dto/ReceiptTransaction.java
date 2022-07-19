package com.hu22.bloodBankBackendPrivate.dto;

import com.sun.istack.NotNull;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ReceiptTransaction {

    private Long id;



    private String bloodGroup;

    private String userName;

    private Long unitOfBloodGroup;

    private String typeOfTransaction;

    private String userEmail;

    private String phone;

    private Date timeStamp;

    private String status;

    private Long bloodBankId;

    private String transactionID;
    ////


    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public ReceiptTransaction() {

    }

    public ReceiptTransaction(Long id,String transactionID, String bloodGroup, String userName, Long unitOfBloodGroup, String typeOfTransaction, String userEmail, String phone, Date timeStamp, String status, Long bloodBankId, String bloodBankName, String city, String contact, String email) {
        this.id = id;
        this.transactionID=transactionID;
        this.bloodGroup = bloodGroup;
        this.userName = userName;
        this.unitOfBloodGroup = unitOfBloodGroup;
        this.typeOfTransaction = typeOfTransaction;
        this.userEmail = userEmail;
        this.phone = phone;
        this.timeStamp = timeStamp;
        this.status = status;
        this.bloodBankId = bloodBankId;
        this.bloodBankName = bloodBankName;
        this.city = city;
        this.contact = contact;
        this.email = email;
    }

    private String bloodBankName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUnitOfBloodGroup() {
        return unitOfBloodGroup;
    }

    public void setUnitOfBloodGroup(Long unitOfBloodGroup) {
        this.unitOfBloodGroup = unitOfBloodGroup;
    }

    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(String typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBloodBankId() {
        return bloodBankId;
    }

    public void setBloodBankId(Long bloodBankId) {
        this.bloodBankId = bloodBankId;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private String city;

    private String contact;

    private String email;


}
