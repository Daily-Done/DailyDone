package com.example.Dailydone.DTO;

import com.example.Dailydone.Entity.UserProfile;
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
    private String Urgency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO customerId;
    private UserDTO runnerId;
    private CategoryDTO categoryId;
    private AddressDTO pickupAddressId;
    private UserProfileDTO userProfileId;
    private UserProfileDTO helperProfileId;

}
