package com.sendiri.transaction.service;

import com.sendiri.repo.dto.request.TopupWalletDto;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;

import java.math.BigDecimal;

public interface
WalletService {

    public Object getBalanceUser(String auth);
    public Object topupBalance(TopupWalletDto request);
    public Object tranferBalance(String auth, TranferWalletRequestDto request);

}
