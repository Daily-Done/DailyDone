package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.EarningStatsDTO;
import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.TempOtp;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.ErrandService;
import com.example.Dailydone.Service.UserprofileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserprofileService userprofileService;
    @Autowired
    private ErrandService errandService;

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    @PostMapping("/create")
    public ResponseEntity<?> CreateProfile(@RequestParam String name,
                                           @RequestParam String dob,
                                           @RequestParam String phone,
                                           @RequestParam String address) throws IOException {

        System.out.println("Create Profile Has been called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setName(name);
        userProfileDTO.setAge(dob);
        userProfileDTO.setPhone(phone);
        userProfileDTO.setLocalAddress(address);
        userProfileDTO.setUserid(UserMapper.toDTO(userPrinciple.GetUser()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userprofileService.CreateProfile(userProfileDTO));
    }

    @PutMapping("/update{id}")
    public ResponseEntity<?> UpdateProfile(@PathVariable Long id,
                                           @RequestBody UserProfileDTO userProfileDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userprofileService.UpdateProfile(id, userProfileDTO));
    }

    @GetMapping("/getProfile")
    public ResponseEntity<?> GetProfile() {

        log.info("getProfile endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long id = userPrinciple.GetUser().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(userprofileService.GetProfile(id));
    }


    @GetMapping("/getHelperProfile")
    public ResponseEntity<?> GetHelperProfile(@RequestParam Long id) {

        log.info("getHelperProfile endpoint called for id: {}", id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(errandService.getHelperProfile(id));
    }

    @GetMapping("/MoneyStats")
    public ResponseEntity<EarningStatsDTO> getEarnings() {

        log.info("MoneyStats endpoint called");

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long userId = userPrinciple.GetUser().getId();

        EarningStatsDTO stats = userprofileService.getMoneyStats(userId);

        log.info("MoneyStats response: {}", stats);

        return ResponseEntity.ok(stats);
    }
}
