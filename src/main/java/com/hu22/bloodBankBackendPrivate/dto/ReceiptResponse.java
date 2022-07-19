package com.hu22.bloodBankBackendPrivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReceiptResponse {

    @NotNull
    private String bloodBankName;
    @NotNull
    private String userName;
    @NotNull
    private String bloodGroup;
    @NotNull
    private Long unitOfBloodGroup;
    @NotNull
    private String typeOfTransaction;
    @NotNull
    private String userEmail;
    @NotNull
    private Date timeStamp;
    @NotNull
    private String status;
}
