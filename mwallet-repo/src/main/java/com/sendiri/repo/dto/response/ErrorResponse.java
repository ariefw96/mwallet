package com.sendiri.repo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String errorCode; // Custom code, misal: 'VAL-001'
    private String message;
    private List<String> details; // Untuk menampung banyak error validasi
}