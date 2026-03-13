package com.sendiri.mwallet_repo.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {

    String phoneNo;
    String pin;
    String otp;
}
