package com.example.Dailydone.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long errandId;
    private Long raterId;
    private Long rateeId;
}
