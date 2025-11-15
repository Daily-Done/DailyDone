package com.example.Dailydone.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Errand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Double price;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(optional = true)
    @JoinColumn(name = "runner_id")
    private User runner;

    private String urgency;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "pickup_address_id")
    private Address pickupAddress;

    @ManyToOne
    @JoinColumn(name = "userProfile_id")
    private UserProfile userProfile;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_id")
    @JsonIgnore
    private Rating rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRating")
    @JsonIgnore
    private RatingForUser ratingForUser;

    @ManyToOne(optional = true)
    @JoinColumn(name = "helperProfile_id")
    private UserProfile helperProfile;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
