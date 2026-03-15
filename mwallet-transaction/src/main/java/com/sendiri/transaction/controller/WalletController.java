package com.sendiri.transaction.controller;

import com.sendiri.repo.dto.request.SearchPagingDto;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.transaction.service.WalletAnalyticService;
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

    @Autowired
    private WalletAnalyticService walletAnalyticService;

    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(
            @RequestHeader(name = "auth") String auth
    ) {
        return new ResponseEntity<>(walletService.getBalanceUser(auth), HttpStatus.OK);
    }

    @PostMapping("/tranfer")
    public ResponseEntity<Object> tranfer(
            @RequestHeader(name = "auth") String auth,
            @RequestBody TranferWalletRequestDto request
    ) {
        return new ResponseEntity<>(walletService.tranferBalance(auth, request), HttpStatus.OK);
    }

    @PostMapping("/history")
    public ResponseEntity<Object> history(
            @RequestHeader(name = "auth") String auth,
            @RequestBody SearchPagingDto searchPaging
    ) {
        return new ResponseEntity<>(walletAnalyticService.history(auth, searchPaging), HttpStatus.OK);
    }

    @GetMapping("/monthly")
    public ResponseEntity<Object> lastMonth(
            @RequestHeader(name = "auth") String auth
    ){
        return new ResponseEntity<>(walletAnalyticService.getMonthlyStats(auth), HttpStatus.OK);
    }



}
