package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.OnlyTokenRequest;
import com.hu22.bloodBankBackendPrivate.services.CampaignRegistrationService;
import com.hu22.bloodBankBackendPrivate.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CampaignRegistrationController {

    @Autowired
    private CampaignRegistrationService campaignRegistrationService;

    @PostMapping("/user/registration/camapign/id/{id}/unit/{unit}")
    public ResponseEntity<?> userCampaignResgistration(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest, @PathVariable("id") Long id, @PathVariable("unit") Long unit){
        String email = tokenToEmail(onlyTokenRequest);
        return campaignRegistrationService.userCampRegistration(email, id, unit);
    }

    @GetMapping("/user/registered/campaign")
    public ResponseEntity<?> userRegisteredCampaigns(@RequestHeader(value="Authorization") OnlyTokenRequest onlyTokenRequest){
        String email = tokenToEmail(onlyTokenRequest);
        return campaignRegistrationService.registeredCampaigns(email);
    }

    public String tokenToEmail(@NotNull OnlyTokenRequest onlyTokenRequest){
        String fullJwt = onlyTokenRequest.getToken();
        String jwt = fullJwt.substring(7,fullJwt.length());
        JwtUtil jwtUtil = new JwtUtil();
        String email = jwtUtil.getEmailFromToken(jwt);
        return email;
    }
}
