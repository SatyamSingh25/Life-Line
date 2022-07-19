package com.hu22.bloodBankBackendPrivate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class OnlyTokenRequest {
    private String token;
    public OnlyTokenRequest(String str){
        this.token = str;
    }
}
