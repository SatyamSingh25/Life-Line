package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.OfflineTransaction;
import com.hu22.bloodBankBackendPrivate.entities.Transaction;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private UserRepository userRepository;

    //Mail for campaign invitation
    public void sendSimpleEmail(String toEmail, String body, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("lifelineresources01@gmail.com"); // my mail
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Send... to " + toEmail ); // write log here
    }

    //offline order confirm mail
    public void orderConfirmMail(String toEmail, OfflineTransaction offlineTransaction){

        BloodBank bloodBankResponse = bloodBankRepository.getById(offlineTransaction.getBloodBankId());

        String name = offlineTransaction.getUserFullName();
        String receiptId = offlineTransaction.getReceiptId();
        String bloodBankName = bloodBankResponse.getBloodBankName();
        String bloodGroup = offlineTransaction.getBloodGroup().toUpperCase();
        Long unit = offlineTransaction.getUnitOfBlood();

        String body = "Hi " + name + ",\n" +
                "Thank you for your order! A record of your purchase information appears below.\n"+
                "Please keep this email for collecting your blood from blood Bank:" + bloodBankName +".\n\n" +
                "ORDER INFORMATION:\n" +
                "Receipt ID: " + receiptId.toUpperCase() + "\n" +
                "Blood Group: " + bloodGroup + "\n" +
                "Unit of " + bloodGroup + ": " + unit + "\n\n" +
                "Thanks again for your business! We appreciate that you've chosen us.\n\n" +
                "Thanks\n"+
                "Life Line || " + bloodBankName + "\n" +
                "email: " + bloodBankResponse.getEmail() + "\n" +
                "+91 " + bloodBankResponse.getContact();

        String subject = "LIFE LINE || Order Confirmed || " + receiptId;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lifelineresources01@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Order Confirmed " + name);
    }

    //online User Order Confirm mail
    public void onlineOrder(String toEmail, Transaction transaction){
        User user = userRepository.findById(toEmail).get();
        BloodBank bloodBank = bloodBankRepository.findById(transaction.getBloodBankId()).get();

        String name = transaction.getUserName().toUpperCase();
        String receiptId = transaction.getTransactionID();
        String bloodBankName = bloodBank.getBloodBankName().toUpperCase();
        String bloodGroup = transaction.getBloodGroup().toUpperCase();
        Long units = transaction.getUnitOfBloodGroup();

        String body = "Hi " + name + ",\n" +
                "Thank you for your order! A record of your purchase information appears below.\n"+
                "Please keep this email for collecting your blood from blood Bank: " + bloodBankName +".\n\n" +
                "ORDER INFORMATION:\n" +
                "Receipt ID: " + receiptId.toUpperCase() + "\n" +
                "Blood Group: " + bloodGroup + "\n" +
                "Unit of " + bloodGroup + " : " + units + "\n\n" +
                "Thanks again for your business! We appreciate that you've chosen us.\n\n" +
                "Thanks\n"+
                "Life Line || " + bloodBankName + "\n" +
                "email: " + bloodBank.getEmail() + "\n" +
                "+91 " + bloodBank.getContact();

        String subject = "LIFE LINE || Order Confirmed || " + receiptId;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lifelineresources01@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Order Confirmed " + name);
    }
}
