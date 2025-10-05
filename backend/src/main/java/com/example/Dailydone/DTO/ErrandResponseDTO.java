package com.example.Dailydone.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrandResponseDTO {
    private ErrandDTO errandDTO;
    private UserProfileDTO userProfileDTO;
}
