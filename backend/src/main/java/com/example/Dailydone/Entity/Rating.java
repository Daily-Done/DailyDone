package com.example.Dailydone.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating;  // 1 to 5
    private String comment;
    private Integer points;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "errand_id")
    @JsonIgnore
    private Errand errand;

    @ManyToOne
    @JoinColumn(name = "rater_id")
    private User rater;

    @ManyToOne
    @JoinColumn(name = "ratedTo_id")
    private User ratee;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

}
