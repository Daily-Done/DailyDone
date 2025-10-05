package com.example.Dailydone.DTO;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
@Data
public class TempSmsOtp {
    private Long tempId;
    private String Otp;
    private String phone;
    private LocalDateTime Expiry;
}
