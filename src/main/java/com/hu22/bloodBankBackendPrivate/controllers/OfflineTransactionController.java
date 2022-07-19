package com.hu22.bloodBankBackendPrivate.controllers;

import com.hu22.bloodBankBackendPrivate.dto.OfflinePurchaseRequest;
import com.hu22.bloodBankBackendPrivate.services.OfflineTransactionService;
import io.swagger.models.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
public class OfflineTransactionController {

    @Autowired
    private OfflineTransactionService offlineTransactionService;

    //making offline transaction by Admin
    @PostMapping("/admin/offline/transaction")
    public ResponseEntity<?> offlinePurchase(@RequestBody OfflinePurchaseRequest offlinePurchaseRequest){
        return offlineTransactionService.offlinePurchase(offlinePurchaseRequest);
    }

    //return list of all offline transaction
    @GetMapping("/admin/offline/transactions")
    public ResponseEntity<?> allOfflineTransactions(){
        return offlineTransactionService.allOfflineTransactions();
    }

    //returning specific offline transaction
    @GetMapping("/admin/offline/transaction/id/{id}")
    public ResponseEntity<?> singleOfflineTransactionInfo(@NotNull @PathVariable("id") Long id){
        return offlineTransactionService.getSingleOfflineTransaction(id);
    }
}
