package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Long> {
     Optional<UserProfile> findByPhone(String phone);
     Optional<UserProfile> findByUser_Id(Long userId);
}
