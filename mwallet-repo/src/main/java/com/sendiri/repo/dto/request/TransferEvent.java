package com.sendiri.repo.dto.request;

public record TransferEvent(
        TranferWalletRequestDto request,
        boolean isSuccess
) {
}