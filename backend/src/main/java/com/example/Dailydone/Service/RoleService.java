package com.example.Dailydone.Service;

import com.example.Dailydone.Entity.Role;
import com.example.Dailydone.Repository.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepo roleRepository;

    public RoleService(RoleRepo roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void saveAllRoles(List<Role> roles) {
        for (Role role : roles) {
            // Check if role already exists by ID or name
            boolean exists = roleRepository.existsById(role.getId()) ||
                    roleRepository.findByName(role.getName()).isPresent();

            if (!exists) {
                roleRepository.save(role);
            }
        }
    }
}
