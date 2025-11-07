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
public class EarningRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;                 // how much earned
    private LocalDateTime earnedAt;        // when earned

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;                     // who earned it (the helper)
}

