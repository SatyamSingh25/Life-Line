package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.entities.Campaign;
import com.hu22.bloodBankBackendPrivate.entities.CampaignRegistration;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.CampResgistrationRepository;
import com.hu22.bloodBankBackendPrivate.repositories.CampaignRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignRegistrationService {

    @Autowired
    private CampResgistrationRepository campResgistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    //User campaign registration
    public ResponseEntity<?> userCampRegistration(String email, Long id, Long unit) {
        User userResponse = userRepository.findById(email).get(); //user response of requesting user

        CampaignRegistration campaignRegistration = new CampaignRegistration(); //new registeration object which is gonna be store in DB

        campaignRegistration.setUserEmail(email);
        campaignRegistration.setBloodBankId(id);
        campaignRegistration.setUnitWantToDonate(unit);

        return new ResponseEntity<>(campResgistrationRepository.save(campaignRegistration), HttpStatus.ACCEPTED);
    }

    //user registered Campaigns
    public ResponseEntity<?> registeredCampaigns(String email) {
        List<CampaignRegistration> userAllRegistrations = campResgistrationRepository.findByUserEmail(email);
        List<Campaign> registeredCampaignsByUser = new ArrayList<>();

        for(CampaignRegistration camp: userAllRegistrations){
            registeredCampaignsByUser.add(campaignRepository.findById(camp.getBloodBankId()).get());
        }

        return new ResponseEntity<>(registeredCampaignsByUser, HttpStatus.OK);
    }
}
