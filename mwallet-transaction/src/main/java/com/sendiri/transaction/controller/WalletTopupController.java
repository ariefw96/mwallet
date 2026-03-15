package com.sendiri.transaction.controller;

import com.sendiri.repo.dto.request.TopupWalletDto;
import com.sendiri.transaction.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/topup")
public class WalletTopupController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<Object> topupWallet(
            @RequestBody TopupWalletDto topupData
    ){
        return new ResponseEntity<>(walletService.topupBalance(topupData), HttpStatus.OK);
    }

}
