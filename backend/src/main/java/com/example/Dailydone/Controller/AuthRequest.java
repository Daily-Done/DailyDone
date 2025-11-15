package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.OTP;
import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.PendingUser;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.External.OTPStorage;
import com.example.Dailydone.External.OtpGeneRater;
import com.example.Dailydone.Repository.PendingUserRepository;
import com.example.Dailydone.Security.JwtUtil;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.EmailService;
import com.example.Dailydone.Service.UserAuthServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthRequest {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserAuthServices userAuthServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PendingUserRepository pendingUserRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> RegisterUser(@RequestBody UserDTO userDTO){
        System.out.println("this signup method is called..");
        if(userAuthServices.verify(userDTO.getEmail()).isPresent()){
            System.out.println("email already exist");
            return ResponseEntity.ok("Email already exist");
        }

        Optional<PendingUser> pendingUser = pendingUserRepository
                .findByEmail(userDTO.getEmail());

        if(pendingUser.isPresent()){
            System.out.println("email already pending");
            throw new RuntimeException("Email already Pending");
        }

        System.out.println("email is perfect");
        String otp = OtpGeneRater.Generate();
        PendingUser pending = new PendingUser();
        pending.setUsername(userDTO.getUsername());
        pending.setEmail(userDTO.getEmail());
        pending.setPassword(userDTO.getPassword());
        pending.setOtp(otp);
        pending.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        System.out.println("saved in pending user");// 5 min expiry
            try {
                pendingUserRepository.save(pending);
                System.out.println("printed successfully no errors");
            }catch (Exception e){
                System.err.println("❌ Error while saving pending user: " + e.getMessage());
                e.printStackTrace();
            }
           System.out.println("got out of that repo");
            try{
            System.out.println("EmailService instance = " + emailService);
            emailService.sendOtp(userDTO.getEmail(), otp);

            return ResponseEntity.ok("Otp has been send");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Signup failed: " + e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> Resend(@RequestParam String email){
        String OTP = OtpGeneRater.Generate();
        PendingUser pendingUser = pendingUserRepository
                .findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Email has not been entered"));

        pendingUser.setOtp(OTP);
        pendingUser.setOtpExpiry(LocalDateTime.now().plusMinutes(1));
        pendingUserRepository.save(pendingUser);
        emailService.sendOtp(pendingUser.getEmail(), pendingUser.getOtp());
        return ResponseEntity.ok("OTP is sent again");

    }

    @PostMapping("/verification")
    public ResponseEntity<?> Verification(@RequestBody OTP otp,@RequestParam String email){
        PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Email not entered yet"));
        if(pendingUser.getOtpExpiry().isBefore(LocalDateTime.now())){
             return ResponseEntity.ok("Otp got expired" +
                     "click below resend link to resend again");
        }
        if(!pendingUser.getOtp().equals(otp.getOtp())){
            return ResponseEntity.ok("Otp is invalid");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(pendingUser.getUsername());
        userDTO.setEmail(pendingUser.getEmail());
        userDTO.setPassword(pendingUser.getPassword());

        userAuthServices.tokenDeletion(pendingUser.getEmail());

        User user = userAuthServices.Register(userDTO);
        UserPrinciple principle = new UserPrinciple(user);
        String token = jwtUtil.generateToken(principle);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping
    public ResponseEntity<?> CancelSignup(@RequestParam String email){
        pendingUserRepository.deleteByEmail(email);
        return ResponseEntity.ok("");
    }


    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestBody UserDTO userDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(userDTO.getEmail(),
                    userDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok("wrong credential");
        }
        UserPrinciple principle = (UserPrinciple) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principle);
        return ResponseEntity.ok(token);
    }
}
