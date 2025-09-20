package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/errand")
public class ErrandController {
    @Autowired
    private ErrandService errandService;
    @PostMapping("/addErrand")
    public ResponseEntity<?> addErrand(@RequestBody ErrandDTO errandDTO){
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        errandDTO.setCustomerId(userPrinciple.GetUser().getId());

        return ResponseEntity.ok(errandService.CreateErrand(errandDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showAll(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(errandService.showErrand(id));
    }

}
