package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Security.JwtUtil;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.UserAuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthRequest {
    @Autowired
    private UserAuthServices userAuthServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/signin")
    public ResponseEntity<?> RegisterUser(@RequestBody UserDTO userDTO){
        User user = userAuthServices.Register(userDTO);
        UserPrinciple principle = new UserPrinciple(user);
        String token = jwtUtil.generateToken(principle);
       return ResponseEntity.ok(token);
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
