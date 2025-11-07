package com.example.Dailydone.Controller;

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
    @PostMapping("/givereview")
    public ResponseEntity<?> giveReview(@RequestParam Double rating,@RequestParam Long id){
        System.out.println("rating is " + rating);
        System.out.println("🧊🧊🧊🧊***************");
        return ResponseEntity.ok(ratingService.giveRating(rating,id));
    }

    @PostMapping("/giveUserReview")
    public ResponseEntity<?> giveUserReview(@RequestParam Double rating,@RequestParam Long id){
        System.out.println("rating for user is " + rating);
        System.out.println("🧊🧊🧊🧊");
        return ResponseEntity.ok(ratingService.giveRating1(rating,id));
    }

}
