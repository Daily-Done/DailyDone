package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.Role;
import com.example.Dailydone.Entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
        }

        return dto;
    }

    // DTO → Entity
    public static User toEntity(UserDTO dto, Role role) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setRole(role); // You must fetch Role before calling this

        return user;
    }
}
