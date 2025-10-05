package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Repository.ErrandRepo;
import com.example.Dailydone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class ValidationController {
    @Autowired
    private ErrandRepo errandRepository;
    @Autowired
    private ErrandMapper errandMapper;
    @Autowired
    private UserRepository userRepository;
    @PutMapping("/errands/{id}/{userId}")
    public ResponseEntity<ErrandDTO> updateStatus(
            @PathVariable Long id,Long userId) {

        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Errand not found"));
        errand.setStatus("Assigned");
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("user not there"));
        errand.setRunner(user);
        errandRepository.save(errand);
    
        return ResponseEntity.ok(errandMapper.toDTO(errand));
    }

}
