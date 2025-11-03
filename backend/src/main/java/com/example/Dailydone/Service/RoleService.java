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

            if (role.getName() == null || role.getName().trim().isEmpty()) continue;

            boolean exists = roleRepository.findByName(role.getName().trim()).isPresent();

            if (!exists) {
                Role newRole = new Role();
                newRole.setName(role.getName().trim());
                roleRepository.save(newRole);
            }
        }
    }
}
