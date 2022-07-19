package com.hu22.bloodBankBackendPrivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OfflinePurchaseRequest {

    private String bloodGroup;
    private Long unitOfBlood;
    private Long bloodBankId;
    private String userFullName;
    private String userEmail;
    private String userPhone;
    private String userAdress;

}
