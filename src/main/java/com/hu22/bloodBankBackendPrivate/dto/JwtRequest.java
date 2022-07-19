package com.hu22.bloodBankBackendPrivate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtRequest {


    private String email;
    private String userPassword;

}
