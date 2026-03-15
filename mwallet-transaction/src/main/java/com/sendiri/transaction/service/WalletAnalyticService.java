package com.sendiri.transaction.service;

import com.sendiri.repo.dto.request.SearchPagingDto;

import java.util.Map;
import java.util.UUID;

public interface WalletAnalyticService {

    public Map<String, Object> getMonthlyStats(String auth, String dayMap);

    public Map<String, Object> history(String auth, SearchPagingDto search);
}
