package com.sendiri.transaction.elastic;

import com.sendiri.repo.dto.request.TranferWalletRequestDto;
import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletHistoryDoc;
import com.sendiri.repo.entity.WalletTrxEntity;
import com.sendiri.transaction.repo.UserRepository;
import com.sendiri.transaction.repo.WalletHistoryDocRepository;
import com.sendiri.transaction.repo.WalletTrxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticService {

    @Autowired
    WalletHistoryDocRepository walletHistoryDocRepository;

    @Autowired
    private WalletTrxRepository walletTrxRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveHistoryToElastic(TranferWalletRequestDto request) {
        WalletTrxEntity trx = walletTrxRepository.findById(request.getTrxId())
                .orElse(null);
        if (trx == null) {
            return;
        }

        WalletHistoryDoc doc = WalletHistoryDoc.builder()
                .walletHistoriesId(String.valueOf(trx.getWalletTrxId()))
                .walletTransaction(WalletHistoryDoc.WalletTransactionDetail.builder()
                        .trxId(String.valueOf(trx.getWalletTrxId()))
                        .balance(trx.getBalance())
                        .transactionDate(trx.getCreatedAt())
                        .toUser(new WalletHistoryDoc.UserInfo(
                                String.valueOf(request.getToUser()),
                                userRepository.findById(request.getToUser())
                                        .map(UserEntity::getPhoneNo).orElse(null)
                        ))
                        .build())
                .build();
        if (request.getFromUser() != null) {
            doc.getWalletTransaction().setFromUser(new WalletHistoryDoc.UserInfo(
                    String.valueOf(request.getFromUser()),
                    userRepository.findById(request.getFromUser())
                            .map(UserEntity::getPhoneNo).orElse(null)
            ));
        }

        walletHistoryDocRepository.save(doc);
    }


}
