package com.sendiri.transaction.controller;

import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.transaction.service.WalletService;
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
            @RequestHeader(name = "auth") String auth
    ) {
        return new ResponseEntity<>(walletService.getBalanceUser(auth), HttpStatus.OK);
    }

    @PostMapping("/topup")
    public ResponseEntity<Object> topupBalance(
            @RequestHeader(name = "auth") String auth
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tranfer")
    public ResponseEntity<Object> tranfer(
            @RequestHeader(name = "auth") String auth,
            @RequestBody TranferWalletRequestDto request
    ) {
        return new ResponseEntity<>(walletService.tranferBalance(auth, request), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Object> history(
            @RequestHeader(name = "auth") String auth
    ) {
        return new ResponseEntity<>(walletService.listHistory(auth), HttpStatus.OK);
    }


}
