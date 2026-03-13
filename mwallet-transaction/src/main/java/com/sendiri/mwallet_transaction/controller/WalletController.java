package com.sendiri.mwallet_transaction.controller;

import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;
import com.sendiri.mwallet_transaction.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(
            @RequestHeader String auth
    ) {
        return new ResponseEntity<>(walletService.getBalanceUser(auth), HttpStatus.OK);
    }

    @PostMapping("/topup")
    public ResponseEntity<Object> topupBalance(
            @RequestHeader String auth
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tranfer")
    public ResponseEntity<Object> tranfer(
            @RequestBody TranferWalletRequestDto request
    ) {
        return new ResponseEntity<>(walletService.tranferBalance(request), HttpStatus.OK);
    }


}
