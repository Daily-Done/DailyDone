package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.UserProfile;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Long> {
     @Lock(LockModeType.PESSIMISTIC_WRITE)
     @Query("SELECT u FROM UserProfile u WHERE u.phone = :phone")
     Optional<UserProfile> findByPhone(String phone);
     Optional<UserProfile> findByUser_Id(Long userId);
}
