package com.example.Dailydone.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class UserProfileDTO {
    private Long id;
    private String name;
    private String phone;
    private Double rating;
    private Double UserBehaviour;
    private UserDTO userid;
    private String age;
    private double earning;
    private int TaskAccepted;
    private int TaskPosted;
    private LocalDateTime createdAT;
    private String localAddress;
}
