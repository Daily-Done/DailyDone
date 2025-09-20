package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.RatingDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.User;

public class RatingMapper {
    public static RatingDTO toDTO(Rating rating) {
        if (rating == null) return null;

        return RatingDTO.builder()
                .id(rating.getId())
                .rating(rating.getRating())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .errandId(rating.getErrand() != null ? rating.getErrand().getId() : null)
                .raterId(rating.getRater() != null ? rating.getRater().getId() : null)
                .rateeId(rating.getRatee() != null ? rating.getRatee().getId() : null)
                .build();
    }

    public static Rating toEntity(RatingDTO dto, Errand errand, User rater, User ratee) {
        if (dto == null) return null;

        return Rating.builder()
                .id(dto.getId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createdAt(dto.getCreatedAt())
                .errand(errand)
                .rater(rater)
                .ratee(ratee)
                .build();
    }

}
