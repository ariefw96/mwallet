package com.sendiri.transaction.service.impl;

import com.sendiri.repo.constant.GenericConstant;
import com.sendiri.repo.constant.TransferStatus;
import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletEntity;
import com.sendiri.repo.entity.WalletTrxEntity;
import com.sendiri.repo.utils.RedisUtil;
import com.sendiri.transaction.kafka.KafkaProducerService;
import com.sendiri.transaction.repo.UserRepository;
import com.sendiri.transaction.repo.WalletHistoryRepository;
import com.sendiri.transaction.repo.WalletRepository;
import com.sendiri.transaction.repo.WalletTrxRepository;
import com.sendiri.transaction.service.WalletService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Transactional()
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private WalletTrxRepository walletTrxRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

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
    public Object tranferBalance(String auth, TranferWalletRequestDto request) {

        UserEntity from = redisUtil.get(auth, UserEntity.class);
        UserEntity to = userRepository.findById(request.getToUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun tidak ditemukan"));

        if(from.getUserId().equals(to.getUserId())){
            System.out.println("tidak dapat tranfer ke wallet sendiri!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TIDAK DAPAT TRANSFER KE WALLET SENDIRI!!");
        }


        ObjectMapper mapper = new ObjectMapper();
        WalletTrxEntity walletTrx = new WalletTrxEntity();
        walletTrx.setWalletTrxId(UUID.randomUUID());
        walletTrx.setFromUser(from.getUserId());
        walletTrx.setToUser(to.getUserId());
        walletTrx.setBalance(request.getBalance());;
        walletTrxRepository.save(walletTrx);

        request.setFromUser(from.getUserId());
        request.setTrxId(walletTrx.getWalletTrxId());

        kafkaProducerService.sendMessage(GenericConstant.KAFKA_TOPIC_WALLET_PENDING, mapper.writeValueAsString(request));

        return Map.of("trxId", walletTrx.getWalletTrxId(), "status", TransferStatus.PENDING);
    }

    @Override
    public Object listHistory(String auth) {
        val usr = redisUtil.get(auth, UserEntity.class);

        return walletHistoryRepository.findAllByWallet(
                walletRepository.findByUser(usr).orElse(null)
        );

    }
}
