package com.example.Dailydone.Controller;

import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.RatingForUser;
import com.example.Dailydone.Repository.ErrandRepo;
import com.example.Dailydone.Repository.RatingRepo;
import com.example.Dailydone.Repository.RatingUserRepo;
import com.example.Dailydone.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private RatingUserRepo ratingUserRepo;
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private ErrandRepo errandRepo;
    @PostMapping("/givereview")
    public ResponseEntity<?> giveReview(@RequestParam Double rating,@RequestParam Long id
            ,@RequestParam long errandId){
        System.out.println("rating is " + rating);
        System.out.println("🧊🧊🧊🧊***************");
        Errand errand = errandRepo.findById(errandId)
                .orElseThrow(()->new RuntimeException("errand not found"));
        if(errand.getRating().getId() != null){
            throw new RuntimeException("User has already given the Rating");
        }
        return ResponseEntity.ok(ratingService.giveRating(rating,id,errandId));
    }

    @PostMapping("/giveUserReview")
    public ResponseEntity<?> giveUserReview(@RequestParam Double rating,@RequestParam Long id
                                            ,@RequestParam Long errandId){

        Errand errand = errandRepo.findById(errandId)
                        .orElseThrow(()->new RuntimeException("errand not found"));
        if(errand.getRatingForUser().getId() != null){
            throw new RuntimeException("Helper has already given the review");
        }
        System.out.println("rating for user is " + rating);
        System.out.println("🧊🧊🧊🧊");
        return ResponseEntity.ok(ratingService.giveRating1(rating,id,errandId));
    }

}
