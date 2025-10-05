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
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;             // Display name
    private String phone;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] profileImage;

    private String age;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private LocalDateTime createdAt;

    private double earning;

    private String LocalAddress;
    private int taskAccepted;
    private int taskPosted;
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
