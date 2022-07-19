package com.hu22.bloodBankBackendPrivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LocationBloodRequest {
    private String city;
    private String bloodGroup;
}
