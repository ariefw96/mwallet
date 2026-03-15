package com.sendiri.transaction.kafka;

import com.sendiri.repo.dto.request.TopupWalletDto;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.transaction.elastic.ElasticService;
import com.sendiri.transaction.service.ProcessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumerService {

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private ElasticService elasticService;

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

    @KafkaListener(topics = "wallet.transfer.success.elastic", groupId = "wallet-group")
    public void saveToElasticAfterSuccess(String message){
        System.out.println("Received message [incoming elastic save]: " + message);
        var val = mapper.readValue(message, TranferWalletRequestDto.class);
        elasticService.saveHistoryToElastic(val);
    }

    @KafkaListener(topics = "wallet.topup", groupId = "wallet-group")
    public void topupBalance(String message){
        System.out.println("Received message [topup]: " + message);
        var val = mapper.readValue(message, TopupWalletDto.class);
        processTransactionService.topupBalance(val);
    }



}