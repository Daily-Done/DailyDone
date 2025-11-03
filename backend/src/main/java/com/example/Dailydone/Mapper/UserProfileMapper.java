package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.Role;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Repository.RoleRepo;
import com.example.Dailydone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepo roleRepo;
    public UserProfileDTO toDTO(UserProfile profile) {
        if (profile == null) return null;

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setPhone(profile.getPhone());

        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(()->new RuntimeException("User Not found"));

        dto.setUserid(UserMapper.toDTO(user));

        dto.setAge(profile.getAge());

        dto.setEarning(profile.getEarning());
        dto.setCreatedAT(profile.getCreatedAt());
        dto.setLocalAddress(profile.getLocalAddress());
        dto.setTaskPosted(profile.getTaskPosted());
        dto.setTaskAccepted(profile.getTaskAccepted());
        return dto;
    }

    public UserProfile toEntity(UserProfileDTO dto) {
        if (dto == null) return null;

        UserProfile profile = new UserProfile();
        profile.setName(dto.getName());
        profile.setPhone(dto.getPhone());
        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("role not there"));
        profile.setUser(UserMapper.toEntity(dto.getUserid(), role));
        profile.setAge(dto.getAge());
        profile.setLocalAddress(dto.getLocalAddress());
        return profile;
    }
}
