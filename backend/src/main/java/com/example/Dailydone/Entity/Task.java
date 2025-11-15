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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;          // e.g., "Cleaning", "Delivery"

    private Double amount;            // money exchanged for task

    private LocalDateTime createdAt; // for daily analytics

    @Column(nullable = true)
    private String status;            // COMPLETED or PENDING or CANCELLED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;         // who created the task

    @ManyToOne
    @JoinColumn(name = "helper_id")
    private UserProfile helper;       // who completed the task
}