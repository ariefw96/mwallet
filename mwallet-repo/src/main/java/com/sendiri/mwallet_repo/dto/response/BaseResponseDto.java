package com.sendiri.mwallet_repo.dto.response;

import lombok.Data;

@Data
public class BaseResponseDto {

    private Integer code;
    private String status;
    private String message;
    private Object data;

}
