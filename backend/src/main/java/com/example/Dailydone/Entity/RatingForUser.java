package com.example.Dailydone.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingForUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating;  // 1 to 5
    private String comment;
    private Integer points;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "errand_id")
    private Errand errand;

    @ManyToOne
    @JoinColumn(name = "ratedTo_id")
    private User ratee;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile1;
}
