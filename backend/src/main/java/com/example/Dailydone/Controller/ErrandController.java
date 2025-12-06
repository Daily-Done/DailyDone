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
import com.example.Dailydone.Service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/errand")
public class ErrandController {

    private static final Logger log = LoggerFactory.getLogger(ErrandController.class);

    @Autowired
    private RatingService ratingService;
    @Autowired
    private ErrandService errandService;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @PostMapping("/addErrand")
    public ResponseEntity<?> addErrand(@RequestBody ErrandDTO errandDTO){
        log.info("add errand service is being called");
        System.out.println("****************************");
        System.out.println("Errand posting api called....");
        System.out.println("*****************************");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        errandDTO.setCustomerId(UserMapper.toDTO(userPrinciple.GetUser()));
        errandService.CreateErrand(errandDTO);
        return ResponseEntity.ok("errand added Successfully");
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

    @PostMapping("/Accept")
    public ResponseEntity<?> AcceptTask(@RequestParam Long id) {
        log.info("Accept task endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.AcceptingTask(id, user));
    }

    @PostMapping("/Cancel")
    public ResponseEntity<?> CancelTask(@RequestParam Long errandId) {
        log.info("Cancel task endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.CancelTask(errandId, user));
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
                                          @RequestParam Long errandId) {

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(() -> new RuntimeException("not found"));

        log.info("TaskCompleted endpoint called");

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.taskCompleted(userProfileId, userProfile.getId(), errandId));
    }

    @GetMapping("/HelperTask")
    public ResponseEntity<?> HelperTask() {

        log.info("HelperTask endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();

        return ResponseEntity.ok(errandService.helperTask(user));
    }

    @GetMapping("/showAllUsersErrands")
    public ResponseEntity<?> showErrands(){

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        return ResponseEntity.ok(errandService.showAllUserErrand(id));
    }

    @GetMapping("/showErrands")
    public ResponseEntity<?> showErrands(
            @RequestParam(required = false) Long catId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("showErrands endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long userId = userPrinciple.GetUser().getId();

        if (catId != null) {
            log.info("Category filter applied -> catId: {}", catId);
            return ResponseEntity.ok(errandService.GetByCategories(page, size, userId, catId));
        }

        log.info("No category filter -> showing all errands");
        return ResponseEntity.ok(errandService.showALlErrands(userId, page, size));
    }

    @GetMapping("/tasks/search")
    public ResponseEntity<Page<ErrandDTO>> searchTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(errandService.searchTasks(keyword, page, size));
    }

    @GetMapping("/getByPriceRange")
    public ResponseEntity<Page<ErrandDTO>> getByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ErrandDTO> errands = errandService.getErrandsByPriceRange(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(errands);
    }

}
