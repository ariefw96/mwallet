package com.sendiri.repo.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopupWalletDto {

    private String phoneNo;
    private BigDecimal balance;
    private String trxId;
}
