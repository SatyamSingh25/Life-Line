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

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OfflineTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String bloodGroup;
    @NotNull
    private Long unitOfBlood;
    @NotNull
    private Date date;
    @NotNull
    private String status;
    @NotNull
    private Long bloodBankId;
    @NotNull
    private String userFullName;
    @NotNull
    private String userEmail;
    @NotNull
    private String userPhone;
    @NotNull
    private String userAddress;
    @NotNull
    private String receiptId;
}
