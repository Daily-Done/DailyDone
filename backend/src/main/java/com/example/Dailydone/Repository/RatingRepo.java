package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByUserProfile(UserProfile userProfile);
    Rating findByErrand_Id(Long id);
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.userProfile.id = :userId")
    Double getAverageRating(@Param("userId") Long userId);
}
