package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.CampaignUpdate;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.Campaign;
import com.hu22.bloodBankBackendPrivate.entities.CampaignRegistration;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import com.hu22.bloodBankBackendPrivate.repositories.CampResgistrationRepository;
import com.hu22.bloodBankBackendPrivate.repositories.CampaignRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private CampResgistrationRepository campResgistrationRepository;

    @Autowired
    private MailService mailService;

    //adding a new campaign and sending automated mails to DONORs which are in campaign city
    public ResponseEntity<?> addCampaign(@NotNull Campaign campaign) {
        campaign.setCity(campaign.getCity().toLowerCase().trim());

        String city = campaign.getCity().toLowerCase();
        BloodBank bank = bloodBankRepository.findById(campaign.getByBloodBankId()).get();
        List<User> cityUsers = userRepository.findByRolesRoleNameAndCity("DONOR", city);
        String bankName = bank.getBloodBankName();

        String body = bankName + " is going to organize a Blood Donation Campaign called " + campaign.getCampaignName().toUpperCase() + ".\n" +
                        "Location detail are as follow:\n" +
                "Venue: " + campaign.getAddress() +"\n\n" +
                    "This charity event can save lives of many people. Hence, we request for your participation and also request you to invite your friends,\n " +
                "relatives and other people you know for this noble cause. ‘A wonderful blood donor took time out from his or her busy life to save life’. Complimentary Goody Bag will be provided for all presenting donors.\n\n"+
                "Should you have any further enquiries or if you would like to join this event, please feel free to contact: " + campaign.getContact()+"\n\n"+
                "Hope to see you in the event.\n\n"+
                "Thank you in advance for your kindly support.";

        //sending mails to campaign city
        for(User user: cityUsers){
            String mail = user.getEmail().toLowerCase();
            String name = user.getFirstName().toUpperCase() + " " + user.getSecondName().toUpperCase();
            String thisMailBody = "Hi " + name + ",\n\n" + body;

            String subject = "LIFE LINE || Invitation to Blood Bank Campaign";

            mailService.sendSimpleEmail(mail, thisMailBody, subject);
        }
        //add name of origanization blood bank
        campaign.setBloodBankName(bank.getBloodBankName().toUpperCase());

        campaign.setCity(campaign.getCity().toLowerCase());
        return new ResponseEntity<>(campaignRepository.save(campaign), HttpStatus.CREATED);
    }

    //editing campaign Blood Details
    public ResponseEntity<?> editCampaign(CampaignUpdate campaignUpdate) {

        Campaign campaign = campaignRepository.getById(campaignUpdate.getId());

        if(campaignUpdate == null){
            return new ResponseEntity<>("requesting campaign NOT FOUND", HttpStatus.OK);
        }

        if(campaignUpdate.getAbnegative() != null){
            campaign.setAbnegative(campaignUpdate.getAbnegative());
        }
        if(campaignUpdate.getAbpositive() != null){
            campaign.setAbpositive(campaignUpdate.getAbpositive());
        }
        if(campaignUpdate.getAnegative() != null){
            campaign.setAnegative(campaignUpdate.getAnegative());
        }
        if(campaignUpdate.getApositive() != null){
            campaign.setApositive(campaignUpdate.getApositive());
        }
        if(campaignUpdate.getOnegative() != null){
            campaign.setOnegative(campaignUpdate.getOnegative());
        }
        if(campaignUpdate.getOpositive() != null){
            campaign.setOpositive(campaignUpdate.getOpositive());
        }
        if(campaignUpdate.getBnegative() != null){
            campaign.setBnegative(campaignUpdate.getBnegative());
        }
        if(campaignUpdate.getBpositive() != null){
            campaign.setBpositive(campaignUpdate.getBpositive());
        }

        return new ResponseEntity<>(campaignRepository.save(campaign), HttpStatus.ACCEPTED);
    }

    //deleting the campaign and Updating collected blood amount to organised blood banks
    public ResponseEntity<?> deletedCampaign(Long id) {

        Campaign campaign = campaignRepository.findById(id).get();

        BloodBank bloodBankResponse = bloodBankRepository.findById(campaign.getByBloodBankId()).get();

        campaign.setDeleted(true);

        if(campaign.getAbnegative() != null){
            bloodBankResponse.setAbnegative(bloodBankResponse.getAbnegative() + campaign.getAbnegative());
        }
        if(campaign.getAbpositive() != null){
            bloodBankResponse.setAbpositive(bloodBankResponse.getAbpositive() + campaign.getAbpositive());
        }
        if(campaign.getAnegative() != null){
            bloodBankResponse.setAnegative(bloodBankResponse.getAnegative() + campaign.getAnegative());
        }
        if(campaign.getApositive() != null){
            bloodBankResponse.setApositive(bloodBankResponse.getApositive() + campaign.getApositive());
        }
        if(campaign.getOnegative() != null){
            bloodBankResponse.setOnegative(bloodBankResponse.getOnegative() + campaign.getOnegative());
        }
        if(campaign.getOpositive() != null){
            bloodBankResponse.setOpositive(bloodBankResponse.getOpositive() + campaign.getOpositive());
        }
        if(campaign.getBnegative() != null){
            bloodBankResponse.setBnegative(bloodBankResponse.getBnegative() + campaign.getBnegative());
        }
        if(campaign.getBpositive() != null){
            bloodBankResponse.setBpositive(bloodBankResponse.getBpositive() + campaign.getBpositive());
        }

        campaignRepository.save(campaign);
        return new ResponseEntity<>(bloodBankRepository.save(bloodBankResponse), HttpStatus.ACCEPTED);
    }

    //open a campaign
    public ResponseEntity<?> openCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id).get();
        campaign.setDeleted(false); //opened the campign
        return new ResponseEntity<>(campaignRepository.save(campaign), HttpStatus.ACCEPTED);
    }

    //return all city active campaigns
    public ResponseEntity<?> cityActiveCampaigns(String city) {
        List<Campaign> listOfCityCampaigns = campaignRepository.findByCityAndIsDeleted(city, false);
        return new ResponseEntity<>(listOfCityCampaigns, HttpStatus.OK);
    }

    //return all active campaigns
    public ResponseEntity<?> allActiveCampaigns() {
        List<Campaign> allActiveCampaigns = campaignRepository.findByIsDeleted(false);
        return new ResponseEntity<>(allActiveCampaigns, HttpStatus.OK);
    }

    //return all campaigns
    public ResponseEntity<?> allCampaigns() {
        List<Campaign> allCampaigns = campaignRepository.findAll();
        return new ResponseEntity<>(allCampaigns, HttpStatus.ACCEPTED);
    }

    //all registrations on specific a specific campaigns
    public ResponseEntity<?> campaignAllRegistrations(Long id) {
        List<CampaignRegistration> allCampaignRegistrations = campResgistrationRepository.findByBloodBankId(id);

        return new ResponseEntity<>(allCampaignRegistrations, HttpStatus.OK);
    }

    //Specific Campaign Details
    public ResponseEntity<?> specificCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id).get();
        if(campaign == null){
            return new ResponseEntity<>("Campaign not found", HttpStatus.NOT_FOUND);
        }
        campaign.setBloodBankName(bloodBankRepository.findById(campaign.getByBloodBankId()).get().getBloodBankName().toLowerCase());
        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }
}
