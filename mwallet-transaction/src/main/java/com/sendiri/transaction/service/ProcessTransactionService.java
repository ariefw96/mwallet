package com.sendiri.transaction.service;

import com.sendiri.repo.constant.GenericConstant;
import com.sendiri.repo.constant.TransferStatus;
import com.sendiri.repo.dto.request.TopupWalletDto;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.repo.dto.request.TransferEvent;
import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletEntity;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    private SSEController sseController;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Value("${event.sse.delay:10000}")
    private Long sseDelay;

    @Transactional
    public void processTransfer(TranferWalletRequestDto request) {

        int debit = walletRepository.debit(request.getFromUser(), request.getBalance());

        if (debit == 0) {
            eventPublisher.publishEvent(new TransferEvent(request, Boolean.FALSE));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }

        walletRepository.credit(request.getToUser(), request.getBalance());
        eventPublisher.publishEvent(new TransferEvent(request, Boolean.TRUE));

    }

    @Transactional
    public void successTransfer(TranferWalletRequestDto request) {
        UserEntity userOut = null;
        if(request.getFromUser() != null){
            userOut = userRepository.findById(request.getFromUser()).orElse(null);
        }
        UserEntity userIn = userRepository.findById(request.getToUser()).orElse(null);
        if (userIn == null) {
            return;
        }
        if(userOut != null){
            WalletHistoryEntity outHistory = new WalletHistoryEntity();
            outHistory.setWallet(userOut.getWallet());
            outHistory.setType(GenericConstant.OUT);
            outHistory.setBalance(request.getBalance());
            outHistory.setTrxId(request.getTrxId().toString());
            outHistory.setCreatedAt(new Date());

            walletHistoryRepository.save(outHistory);
        }

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
            Thread.sleep(this.sseDelay); //for testing, changed in app yaml
            sendSSE(trx);
        }catch (InterruptedException ie){
            System.out.print("thread interupted");
        }

    }

    @Transactional
    public void topupBalance(TopupWalletDto req){
        UserEntity usr = userRepository.findByPhoneNo(req.getPhoneNo()).orElse(null);
        if(usr == null){
            return;
        }
        WalletEntity wl = walletRepository.findByUser(usr).orElse(null);
        if(wl == null){
            return;
        }
        wl.setBalance(wl.getBalance().add(req.getBalance()));
        walletRepository.save(wl);

        TranferWalletRequestDto tf = new TranferWalletRequestDto();
        tf.setTrxId(UUID.fromString(req.getTrxId()));
        tf.setBalance(req.getBalance());
        tf.setToUser(usr.getUserId());

        eventPublisher.publishEvent(new TransferEvent(tf, Boolean.TRUE));

    }

    private void sendSSE(WalletTrxEntity trx){
        sseController.pushTransactionEvent(String.valueOf(trx.getWalletTrxId()), trx);
    }

}
