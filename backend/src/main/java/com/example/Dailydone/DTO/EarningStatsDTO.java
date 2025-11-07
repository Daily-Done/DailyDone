package com.example.Dailydone.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningStatsDTO {
    private Double totalEarnings;
    private Double monthlyEarnings;
    private Double weeklyEarnings;
    private Double dailyEarnings;
}
