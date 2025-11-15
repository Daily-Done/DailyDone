package com.example.Dailydone.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private Double totalMoneyExchangeToday;
    private String mostRequestedCategoryToday;
    private Long categoryCountToday;
    private Long tasksToday;
    private List<UserProfileDTO> topHelpers;
    private List<UserProfileDTO> highestEarningHelpers;
}
