package com.sendiri.repo.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TranferWalletRequestDto {

    private UUID trxId;
    private UUID fromUser;
    private UUID toUser;
    private BigDecimal balance;

}
