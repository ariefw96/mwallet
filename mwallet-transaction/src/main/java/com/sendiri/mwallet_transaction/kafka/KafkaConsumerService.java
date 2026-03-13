package com.sendiri.mwallet_transaction.kafka;

import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;
import com.sendiri.mwallet_transaction.service.ProcessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumerService {

    @Autowired
    private ProcessTransactionService processTransactionService;

    @KafkaListener(topics = "wallet.transfer", groupId = "wallet-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        ObjectMapper mapper = new ObjectMapper();
        var tranferReq = mapper.readValue(message, TranferWalletRequestDto.class);

        processTransactionService.processTransfer(tranferReq);
    }
}