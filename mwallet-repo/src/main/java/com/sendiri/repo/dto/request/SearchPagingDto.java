package com.sendiri.repo.dto.request;

import lombok.Data;

@Data
public class SearchPagingDto {

    private String searchKey;
    private String searchValue;
    private int page = 1;
    private int size = 10;
}
