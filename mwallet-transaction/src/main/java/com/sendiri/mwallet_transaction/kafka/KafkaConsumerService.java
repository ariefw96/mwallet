package com.sendiri.mwallet_transaction.kafka;

import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;
import com.sendiri.mwallet_transaction.service.ProcessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumerService {

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(topics = "wallet.transfer", groupId = "wallet-group")
    public void listenWalletTranfer(String message) {
        System.out.println("Received message[start tranfer]: " + message);
        var val = mapper.readValue(message, TranferWalletRequestDto.class);

        processTransactionService.processTransfer(val);
    }

    @KafkaListener(topics = "wallet.transfer.success", groupId = "wallet-group")
    public void listenWalletSuccess(String message) {
        System.out.println("Received message [tranfer success]: " + message);
        var val = mapper.readValue(message, TranferWalletRequestDto.class);
        processTransactionService.successTransfer(val);

    }

    @KafkaListener(topics = "wallet.transfer.failed", groupId = "wallet-group")
    public void listenWalletFailed(String message) {
        System.out.println("Received message [tranfer failed]: " + message);
        var val = mapper.readValue(message, TranferWalletRequestDto.class);
        processTransactionService.failedTranfer(val);
    }


}