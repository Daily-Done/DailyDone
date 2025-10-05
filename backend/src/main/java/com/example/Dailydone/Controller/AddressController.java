package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.AddressDTO;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Security.UserPrinciple;
import com.example.Dailydone.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @PostMapping("/address")
    public ResponseEntity<?> CreateAddress(@RequestBody AddressDTO addressDTO){

        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        addressDTO.setUserId(UserMapper.toDTO(userPrinciple.GetUser()));

       return ResponseEntity.status(HttpStatus.CREATED)
               .body(addressService.saveAddress(addressDTO));
    }
    @GetMapping("/getAddress/{id}")
    public ResponseEntity<?> getUserPreviousAddresses(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(addressService.getAddress(id));
    }
}
