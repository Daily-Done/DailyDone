package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.RatingForUser;
import com.example.Dailydone.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingUserRepo extends JpaRepository<RatingForUser,Long> {
    List<RatingForUser> findByUserProfile1(UserProfile userProfile);
    RatingForUser findByErrand_Id(Long id);
}
