package com.sendiri.mwallet_transaction.service;

import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;

import java.math.BigDecimal;

public interface
WalletService {

    public Object getBalanceUser(String auth);
    public Object topupBalance(String auth, BigDecimal balance);
    public Object tranferBalance(String auth, TranferWalletRequestDto request);
    public Object listHistory(String auth);

}
