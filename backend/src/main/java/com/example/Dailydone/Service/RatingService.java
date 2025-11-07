package com.example.Dailydone.Service;

import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.RatingForUser;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Repository.RatingRepo;
import com.example.Dailydone.Repository.RatingUserRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private RatingUserRepo ratingUserRepo;

    public String giveRating(Double rating, Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating1 = new Rating();
        rating1.setRating(rating);
        rating1.setPoints((int) (rating * 10)); // safe, rating is set
        rating1.setUserProfile(userProfile);

        System.out.println(rating1.getUserProfile().getId() + " ✅✅");
        ratingRepo.save(rating1);
        return userProfile.getName();
    }

    public String giveRating1(Double rating, Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RatingForUser rating1 = new RatingForUser();
        rating1.setRating(rating);              // ✅ set rating before using it
        rating1.setPoints((int) (rating * 10)); // ✅ safe math
        rating1.setUserProfile1(userProfile);

        System.out.println(rating1.getUserProfile1().getId() + " ✅✅");
        ratingUserRepo.save(rating1);
        return userProfile.getName();
    }
}
