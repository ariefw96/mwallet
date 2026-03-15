package com.sendiri.transaction.service.impl;

import com.sendiri.repo.dto.request.SearchPagingDto;
import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletHistoryDoc;
import com.sendiri.repo.utils.RedisUtil;
import com.sendiri.transaction.repo.WalletHistoryDocRepository;
import com.sendiri.transaction.service.WalletAnalyticService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WalletAnalyticsServiceImpl implements WalletAnalyticService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private WalletHistoryDocRepository searchRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Map<String, Object> getMonthlyStats(String auth, String key) {

        val usr = redisUtil.get(auth, UserEntity.class);

        Map<String, Integer> daymap = Map.of("1d", 1, "7d", 7, "30d", 30);
        if(daymap.get(key) == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid");
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -daymap.get(key));
        Date thirtyDaysAgo = cal.getTime();

        List<WalletHistoryDoc> outList = searchRepository
                .findByWalletTransactionFromUserPhoneNoAndWalletTransactionTransactionDateAfter(usr.getPhoneNo(), thirtyDaysAgo);

        List<WalletHistoryDoc> inList = searchRepository
                .findByWalletTransactionToUserPhoneNoAndWalletTransactionTransactionDateAfter(usr.getPhoneNo(), thirtyDaysAgo);

        BigDecimal totalOut = outList.stream()
                .map(doc -> doc.getWalletTransaction().getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIn = inList.stream()
                .map(doc -> doc.getWalletTransaction().getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("phoneNo", usr.getPhoneNo());
        result.put("period", "Last "+daymap.get("key")+(" day(s)"));

        result.put("pengeluaran", Map.of(
                "total", totalOut,
                "count", outList.size(),
                "transactions", outList
        ));

        result.put("pemasukan", Map.of(
                "total", totalIn,
                "count", inList.size(),
                "transactions", inList
        ));

        return result;
    }

    @Override
    public Map<String, Object> history(String auth, SearchPagingDto search) {
        var usr = redisUtil.get(auth, UserEntity.class);
        String phoneNo = usr.getPhoneNo();
        PageRequest pageable = PageRequest.of(
                search.getPage()-1,
                search.getSize(),
                Sort.by(Sort.Direction.DESC, "walletTransaction.transactionDate")
        );

        Page<WalletHistoryDoc> elasticPage = searchRepository
                .findByWalletTransactionFromUserPhoneNoOrWalletTransactionToUserPhoneNo(
                        phoneNo, phoneNo, pageable);

//        Page<WalletHistoryDoc> elasticPage = searchRepository.findAll(pageable);

        List<Map<String, Object>> historyList = elasticPage.getContent().stream().map(doc -> {
            var trx = doc.getWalletTransaction();

            String type = "IN";
            if (trx.getFromUser() != null) {
                type = trx.getFromUser().getPhoneNo().equals(phoneNo) ? "OUT" : "IN";
            }

            Map<String, Object> item = new HashMap<>();
            item.put("trxId", trx.getTrxId());
            item.put("balance", trx.getBalance());
            item.put("type", type);
            item.put("transactionDate", trx.getTransactionDate());

            return item;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("phoneNo", phoneNo);
        result.put("history", historyList);
        result.put("page", search.getPage());
        result.put("size", elasticPage.getSize());
        result.put("totalElements", elasticPage.getTotalElements());

        return result;
    }
}