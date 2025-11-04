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
    @Column(name = "name")
    private String name;             // Display name
    private String phone;

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

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
