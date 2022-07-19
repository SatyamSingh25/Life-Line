package com.hu22.bloodBankBackendPrivate.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String transactionID;

    @NotNull
    private String bloodGroup;
    @NotNull
    private String userName;
    @NotNull
    private Long unitOfBloodGroup;
    @NotNull
    private String typeOfTransaction;
    @NotNull
    private String userEmail;
    @NotNull
    private String phone;
    @NotNull
    private Date timeStamp;
    @NotNull
    private String status;
    @NotNull
    private Long bloodBankId;
}
