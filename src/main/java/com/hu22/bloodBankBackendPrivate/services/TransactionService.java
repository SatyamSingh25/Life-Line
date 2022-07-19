package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.OnlyTokenRequest;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.TransactionRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    //show all transaction list
    public ResponseEntity<?> allTransactions(){

        if(transactionRepository.findAll().size()==0){
            return new ResponseEntity<>("Transactions not fond", HttpStatus.OK);
        }
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.ACCEPTED);
      
    }
    //show a particular user transaction
    public ResponseEntity<?> userAllTransactions(String email) {

        User userResponse = userRepository.findById(email).get();
        if(userResponse == null){
            return new ResponseEntity<>("User not fond", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transactionRepository.findByUserEmail(email), HttpStatus.ACCEPTED);
    }

    public String deleteTransactions() {

        transactionRepository.deleteAll();
        return "deleted";
    }
}
