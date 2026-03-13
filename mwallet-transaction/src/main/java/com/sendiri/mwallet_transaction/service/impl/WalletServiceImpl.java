package com.sendiri.mwallet_transaction.service.impl;

import com.sendiri.mwallet_repo.constant.GenericConstant;
import com.sendiri.mwallet_repo.dto.request.TranferWalletRequestDto;
import com.sendiri.mwallet_repo.entity.UserEntity;
import com.sendiri.mwallet_repo.entity.WalletEntity;
import com.sendiri.mwallet_repo.utils.RedisUtil;
import com.sendiri.mwallet_transaction.kafka.KafkaProducerService;
import com.sendiri.mwallet_transaction.repo.WalletRepository;
import com.sendiri.mwallet_transaction.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public Object getBalanceUser(String auth) {
        UserEntity user = redisUtil.get(auth, UserEntity.class);
        WalletEntity wallet = walletRepository.findByUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wallet tidak ditemukan")
        );
        return Map.of("balance", wallet.getBalance());

    }

    @Override
    public Object topupBalance(String auth, BigDecimal balance) {
        UserEntity user = redisUtil.get(auth, UserEntity.class);
        WalletEntity wallet = walletRepository.findByUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wallet tidak ditemukan")
        );
        wallet.setBalance(wallet.getBalance().add(balance));
        return Map.of("balance", wallet.getBalance());
    }

    @Override
    public Object tranferBalance(TranferWalletRequestDto request) {
        ObjectMapper mapper = new ObjectMapper();
        UUID trxId = UUID.randomUUID();
        request.setTrxId(trxId);
        kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC, mapper.writeValueAsString(request));
        return Map.of("trxId", trxId);
    }
}
