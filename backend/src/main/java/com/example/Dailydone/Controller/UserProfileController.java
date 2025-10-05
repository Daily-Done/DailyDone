package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.TempOtp;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.UserprofileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/create")
    public ResponseEntity<?> CreateProfile(@RequestParam String name,
                                           @RequestParam String dob,
                                           @RequestParam String phone,
                                           @RequestParam String address,
                                           @RequestParam("image") MultipartFile image) throws IOException {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setProfileImage(image.getBytes());
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

    @GetMapping("/profile{id}")
    public ResponseEntity<?> GetProfile(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(userprofileService.GetProfile(id));
    }

    @PostMapping("/sendSms")
    public ResponseEntity<?> SendPhone(@RequestParam String phone) {
        return ResponseEntity.status(HttpStatus.CONTINUE)
                .body(userprofileService.sendOTP(phone));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/verifySms")
    public ResponseEntity<?> SendOtp(@RequestParam String phone,@RequestBody TempOtp otp){
        return ResponseEntity.status(HttpStatus.CONTINUE)
                .body(userprofileService.Verify(otp,phone));
    }
}
