package com.sendiri.transaction.kafka;

import com.sendiri.repo.constant.GenericConstant;
import com.sendiri.repo.dto.request.TransferEvent;
import com.sendiri.transaction.elastic.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

@Component
public class TransferEventListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ElasticService elasticService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSuccess(TransferEvent event) {
        if (event.isSuccess()) {
            kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_WALLET_SUCCESS, mapper.writeValueAsString(event.request()));
            kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_ELASTIC_SAVE_WALLET_SUCCESS, mapper.writeValueAsString(event.request()));
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRollback(TransferEvent event) {
        if (!event.isSuccess()) {
            kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_WALLET_FAILED, mapper.writeValueAsString(event.request()));
        }
    }
}