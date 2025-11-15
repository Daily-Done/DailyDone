package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findByEmail(String email);
    void deleteByEmail(String email);
    @Query("SELECT p FROM PendingUser p WHERE p.createdAt < :timeLimit")
    List<PendingUser> findOlderThan(@Param("timeLimit") LocalDateTime timeLimit);
}
