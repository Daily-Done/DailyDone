package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.Role;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Repository.RoleRepo;
import com.example.Dailydone.Repository.UserRepository;
import com.example.Dailydone.Security.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthServices implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User Register(UserDTO userDTO) {
     Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
     if(user.isPresent()){
         throw new RuntimeException("Username already there");
     }
        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("role not there"));
        User user1 = UserMapper.toEntity(userDTO,role);
        user1.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user1 = userRepository.save(user1);
        return user1;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email is not registered"));
        return new UserPrinciple(user);
    }
}
