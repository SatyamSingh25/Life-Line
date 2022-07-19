package com.hu22.bloodBankBackendPrivate.dto;

import com.hu22.bloodBankBackendPrivate.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {

    @Autowired
    private User user;
    private String token;

    public JwtResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
