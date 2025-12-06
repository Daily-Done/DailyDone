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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(RatingService.class);

    @Transactional
    public String giveRating(Double rating, Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating1 = new Rating();
        rating1.setRating(rating);
        rating1.setPoints((int) (rating * 10));
        rating1.setUserProfile(userProfile);

        log.info("Rating added for userProfileId: {}", rating1.getUserProfile().getId());

        ratingRepo.save(rating1);
        return userProfile.getName();
    }

    @Transactional
    public String giveRating1(Double rating, Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RatingForUser rating1 = new RatingForUser();
        rating1.setRating(rating);
        rating1.setPoints((int) (rating * 10));
        rating1.setUserProfile1(userProfile);

        log.info("RatingForUser added for userProfileId: {}", rating1.getUserProfile1().getId());

        ratingUserRepo.save(rating1);

        return userProfile.getName();
    }

}
