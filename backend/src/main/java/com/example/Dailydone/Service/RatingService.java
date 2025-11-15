package com.example.Dailydone.Service;

import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.RatingForUser;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Repository.ErrandRepo;
import com.example.Dailydone.Repository.RatingRepo;
import com.example.Dailydone.Repository.RatingUserRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import jakarta.transaction.Transactional;
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
    @Autowired
    private ErrandRepo err;

    @Transactional
    public String giveRating(Double rating, Long id,Long errId) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Errand errand = err.findById(errId)
                .orElseThrow(()->new RuntimeException("errand not found"));

        Rating rating1 = new Rating();
        rating1.setRating(rating);
        rating1.setPoints((int) (rating * 10)); // safe, rating is set
        rating1.setUserProfile(userProfile);
        errand.setRating(rating1);
        System.out.println(rating1.getUserProfile().getId() + " ✅✅");
        ratingRepo.save(rating1);
        err.save(errand);
        return userProfile.getName();
    }
    @Transactional
    public String giveRating1(Double rating, Long id,Long errId) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Errand errand = err.findById(errId)
                .orElseThrow(()->new RuntimeException("errand not found"));

        RatingForUser rating1 = new RatingForUser();
        rating1.setRating(rating);              // ✅ set rating before using it
        rating1.setPoints((int) (rating * 10)); // ✅ safe math
        rating1.setUserProfile1(userProfile);
        errand.setRatingForUser(rating1);
        System.out.println(rating1.getUserProfile1().getId() + " ✅✅");

        ratingUserRepo.save(rating1);
        err.save(errand);
        return userProfile.getName();
    }

}
