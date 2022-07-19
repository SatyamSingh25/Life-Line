package com.hu22.bloodBankBackendPrivate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter @Setter @NoArgsConstructor
public class UserUpdateInfo {

    private String email;
    private String firstName;
    private String secondName;
    private String password;
    private String dob;
    private String bloodGroup;
    private String city;
    private String phone;
}
