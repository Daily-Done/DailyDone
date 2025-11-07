package com.example.Dailydone.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String description;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserProfileDTO customerId;
    private UserDTO runnerId;
    private CategoryDTO categoryId;
    private AddressDTO pickupAddressId;
    private UserProfileDTO userProfileId;
    private UserProfileDTO helperProfileId;
    private String Urgency;

}
