package com.example.Dailydone.Entity;

import com.example.Dailydone.DTO.RatingDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    private String phone;
    private Double rating;
    private Double UserRating;
    @Column(name = "age")
    private String age;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "earning")
    private double earning;

    @Column(name = "LocalAddress")
    private String LocalAddress;
    @Column(name = "taskAccepted")
    private int taskAccepted;
    @Column(name = "taskPosted")
    private int taskPosted;

    @OneToOne
    @JoinColumn(name = "earningRecord_id")
    private EarningRecord earningRecord;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "userProfile1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingForUser> ratingUser = new ArrayList<>();

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
