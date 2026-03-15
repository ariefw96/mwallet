package com.sendiri.transaction.repo;

import com.sendiri.repo.entity.WalletHistoryDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WalletHistoryDocRepository extends ElasticsearchRepository<WalletHistoryDoc, String> {

        List<WalletHistoryDoc> findByWalletTransactionFromUserPhoneNoAndWalletTransactionTransactionDateAfter(
                String phoneNo, Date startDate);

        List<WalletHistoryDoc> findByWalletTransactionToUserPhoneNoAndWalletTransactionTransactionDateAfter(
                String phoneNo, Date startDate);

    Page<WalletHistoryDoc> findByWalletTransactionFromUserPhoneNoOrWalletTransactionToUserPhoneNo(
            String fromPhone,
            String toPhone,
            Pageable pageable
    );

}