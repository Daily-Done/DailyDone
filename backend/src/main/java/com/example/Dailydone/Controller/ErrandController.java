package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Mapper.UserMapper;
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

        errandDTO.setCustomerId(UserMapper.toDTO(userPrinciple.GetUser()));

        return ResponseEntity.ok(errandService.CreateErrand(errandDTO));
    }

    @GetMapping("/showUserErrands")
    public ResponseEntity<?> showAllUsers(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(errandService.showAcceptedErrand(id));
    }

    @GetMapping("/showHelperErrands")
    public ResponseEntity<?> showAllHelpers(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(errandService.showAcceptedErrand1(id));
    }

    @PostMapping("/Accept/{userid}")
    public ResponseEntity<?> AcceptTask(@PathVariable Long errandId){
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userPrinciple.GetUser();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.AcceptingTask(errandId,user));
    }

    @PostMapping("/Cancel/{id}")
    public ResponseEntity<?> CancelTask(@PathVariable Long errandId){
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrinciple.GetUser();
        return ResponseEntity.status(HttpStatus.CONTINUE)
                .body(errandService.CancelTask(errandId,user));
    }

    @PostMapping("/TaskCompleted/{id}")
    public ResponseEntity<?> CompleteTAsk(@RequestParam Long helperProfileId,
                                          @RequestParam Long userProfileId,
                                          @PathVariable Long errandId){


        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(errandService.taskCompleted(userProfileId,helperProfileId,errandId));
    }

}
