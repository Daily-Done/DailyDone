package com.example.Dailydone.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrandDTO {
    private Long id;
    private String description;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long customerId;
    private Long runnerId;
    private Long categoryId;
    private Long pickupAddressId;

}
