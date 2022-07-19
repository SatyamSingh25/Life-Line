package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.CampaignUpdate;
import com.hu22.bloodBankBackendPrivate.entities.Campaign;
import com.hu22.bloodBankBackendPrivate.entities.Role;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private UserRepository userRepository;

    //adding new Campaign
    @PostMapping("/admin/campaign")
    public ResponseEntity<?> addCampaign(@RequestBody Campaign campaign){
        return campaignService.addCampaign(campaign);
    }

    //edit specific campaign details
    @PutMapping("/admin/campaign/update/blooddetails")
    public ResponseEntity<?> editCampaign(@RequestBody CampaignUpdate campaignUpdate){
        return campaignService.editCampaign(campaignUpdate);
    }

    //Deleting a campaign by ID
    @PutMapping("/admin/campaign/close/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable("id") Long id){
        return campaignService.deletedCampaign(id);
    }

    @PutMapping("/admin/campaign/open/{id}")
    public ResponseEntity<?> openCampaign(@PathVariable("id") Long id){
        return campaignService.openCampaign(id);
    }

    //Returning list of City-wise ACTIVE campaigns
    @GetMapping("/campaigns/active/city/{city}")
    public ResponseEntity<?> cityActiveCampaigns(@PathVariable("city") String city){
        return campaignService.cityActiveCampaigns(city.toLowerCase());
    }

    //Return List of All ACTIVE Campaigns
    @GetMapping("/campaigns/active")
    public ResponseEntity<?> allActiveCampaigns(){
        return campaignService.allActiveCampaigns();
    }

    //Return List of All Campaigns
    @GetMapping("/campaigns")
    public ResponseEntity<?> allCampaigns(){
        return campaignService.allCampaigns();
    }

    @GetMapping("/admin/campaign/registrations/id/{id}")
    public ResponseEntity<?> registrationsOnCampaign(@PathVariable("id") Long id){
        return campaignService.campaignAllRegistrations(id);
    }

    //getting a specific Campaign Details
    @GetMapping("/campaign/{id}")
    public ResponseEntity<?> specificCampaign(@PathVariable("id") Long id){
        return campaignService.specificCampaign(id);
    }

}

















