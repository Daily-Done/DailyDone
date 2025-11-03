package com.example.Dailydone.Controller;

import com.example.Dailydone.Entity.Role;
import com.example.Dailydone.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // POST /api/roles/add-all
    @PostMapping("/add-all")
    public ResponseEntity<String> addRoles(@RequestBody List<Role> roles) {
        roleService.saveAllRoles(roles);
        return ResponseEntity.ok("✅ Roles inserted successfully into database.");
    }
}