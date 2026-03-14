package com.sendiri.transaction.service;

import com.sendiri.repo.constant.GenericConstant;
import com.sendiri.repo.constant.TransferStatus;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletHistoryEntity;
import com.sendiri.repo.entity.WalletTrxEntity;
import com.sendiri.transaction.controller.SSEController;
import com.sendiri.transaction.kafka.KafkaProducerService;
import com.sendiri.transaction.repo.UserRepository;
import com.sendiri.transaction.repo.WalletHistoryRepository;
import com.sendiri.transaction.repo.WalletRepository;
import com.sendiri.transaction.repo.WalletTrxRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

@Service
public class ProcessTransactionService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Autowired
    private WalletTrxRepository walletTrxRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private SSEController sseController;

    @Autowired
    private ObjectMapper mapper;

    @Transactional
    public void processTransfer(TranferWalletRequestDto request) {

        int debit = walletRepository.debit(request.getFromUser(), request.getBalance());

        if (debit == 0) {
            kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_WALLET_FAILED, mapper.writeValueAsString(request));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }

        walletRepository.credit(request.getToUser(), request.getBalance());
        kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_WALLET_SUCCESS, mapper.writeValueAsString(request));
    }

    @Transactional
    public void successTransfer(TranferWalletRequestDto request) {
        UserEntity userOut = userRepository.findById(request.getFromUser()).orElse(null);
        UserEntity userIn = userRepository.findById(request.getToUser()).orElse(null);
        if (userIn == null || userOut == null) {
            return;
        }
        WalletHistoryEntity outHistory = new WalletHistoryEntity();
        outHistory.setWallet(userOut.getWallet());
        outHistory.setType(GenericConstant.OUT);
        outHistory.setBalance(request.getBalance());
        outHistory.setTrxId(request.getTrxId().toString());
        outHistory.setCreatedAt(new Date());

        walletHistoryRepository.save(outHistory);

        WalletHistoryEntity inHistory = new WalletHistoryEntity();
        inHistory.setWallet(userIn.getWallet());
        inHistory.setType(GenericConstant.IN);
        inHistory.setBalance(request.getBalance());
        inHistory.setTrxId(request.getTrxId().toString());
        inHistory.setCreatedAt(new Date());

        walletHistoryRepository.save(inHistory);

        doUpdateWalletTrx(request.getTrxId(), TransferStatus.SUCCESS);

    }

    @Transactional
    public void failedTranfer(TranferWalletRequestDto request) {
        doUpdateWalletTrx(request.getTrxId(), TransferStatus.FAILED);
    }

    private void doUpdateWalletTrx(UUID trxId, TransferStatus status) {
        WalletTrxEntity trx = walletTrxRepository.findById(trxId)
                .orElse(null);
        if(trx == null){
            return;
        }
        trx.setStatus(status);
        trx.setUpdatedAt(new Date());

        walletTrxRepository.save(trx);

        try{
            Thread.sleep(10000); //10s delay for testing
            sendSSE(trx);
        }catch (InterruptedException ie){
            System.out.print("thread interupted");
        }

    }

    private void sendSSE(WalletTrxEntity trx){
        sseController.pushTransactionEvent(String.valueOf(trx.getWalletTrxId()), trx);
    }

}
