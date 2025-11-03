package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.RatingDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Repository.UserProfileRepo;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/errand")
public class ErrandController {
    @Autowired
    private ErrandService errandService;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @PostMapping("/addErrand")
    public ResponseEntity<?> addErrand(@RequestBody ErrandDTO errandDTO){
        System.out.println("****************************");
        System.out.println("Errand posting api called....");
        System.out.println("*****************************");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        errandDTO.setCustomerId(UserMapper.toDTO(userPrinciple.GetUser()));
        errandService.CreateErrand(errandDTO);
        return ResponseEntity.ok("errand added Successfully");
    }

    @GetMapping("/showErrands")
    public ResponseEntity<?> showErrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        System.out.println("🫡 showErrands endpoint has been called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        return ResponseEntity.ok(errandService.showALlErrands(id, page, size));
    }

    @GetMapping("/showCompletedErrands")
    public ResponseEntity<?> showAllUsers(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(errandService.showAcceptedErrand(id));
    }

    @GetMapping("/showHelperErrands")
    public ResponseEntity<?> showAllHelpers(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(errandService.showAcceptedErrand1(id));
    }

    @GetMapping("/showAllUsersErrands")
    public ResponseEntity<?> showErrands(){

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        return ResponseEntity.ok(errandService.showAllUserErrand(id));
    }

    @PostMapping("/Accept")
    public ResponseEntity<?> AcceptTask(@RequestParam Long id){
        System.out.println("🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉 Accept task has been called");
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.AcceptingTask(id,user));
    }

    @PostMapping("/Cancel")
    public ResponseEntity<?> CancelTask(@RequestParam Long errandId){
        System.out.println("🩻🩻🩻🩻🔬🔬🔬🔬");
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrinciple.GetUser();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.CancelTask(errandId,user));
    }

    @DeleteMapping("/CancelUserErrand")
    public ResponseEntity<?> CancelErrand(@RequestParam Long id){
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long userid = userPrinciple.GetUser().getId();
        return ResponseEntity.ok(errandService.deleted(id,userid));
    }

    @PostMapping("/TaskCompleted")
    public ResponseEntity<?> CompleteTAsk(@RequestParam Long userProfileId,
                                          @RequestParam Long errandId){

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                        .orElseThrow(()->new RuntimeException("not found"));

        System.out.println("this method has been called 🍴🍴🍴🧊🧊🧊");
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.taskCompleted(userProfileId,userProfile.getId(),errandId));
    }

    @GetMapping("/HelperTask")
    public ResponseEntity<?> HelperTask(){
        System.out.println("🦧🦧🦧🦧🦧🦧 this method is called");
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();

        return ResponseEntity.ok(errandService.helperTask(user));

    }

    @GetMapping("/getByCategory")
    public ResponseEntity<?> GetByCategory(@RequestParam Long catId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size){

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id1 = userPrinciple.GetUser().getId();

        return ResponseEntity.ok(errandService);
    }

    @PostMapping("/review")
    public ResponseEntity<?> Review(@RequestBody RatingDTO ratingDTO){
       return ResponseEntity.ok("yashraj");
    }

}
