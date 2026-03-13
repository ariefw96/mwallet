package com.sendiri.mwallet_transaction.service;

import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;
import com.sendiri.mwallet_transaction.repo.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessTransactionService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void processTransfer(TranferWalletRequestDto request) {

        int debit = walletRepository.debit(request.getFromUser(), request.getBalance());

        if (debit == 0) {
            throw new RuntimeException("Insufficient balance");
        }

        walletRepository.credit(request.getToUser(), request.getBalance());
    }

}
