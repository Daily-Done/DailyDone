package com.example.Dailydone.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryDTO {
    private Long id;
    private String status;
    private LocalDateTime changedAt;
    private Long errandId;
}
