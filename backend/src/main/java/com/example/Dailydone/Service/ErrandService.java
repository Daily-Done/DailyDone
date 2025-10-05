package com.example.Dailydone.Service;

import com.example.Dailydone.Controller.ErrandWebSocketController;
import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.ErrandResponseDTO;
import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Mapper.UserProfileMapper;
import com.example.Dailydone.Repository.ErrandRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import com.example.Dailydone.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ErrandService {
    @Autowired
    private ErrandRepo errandRepo;
    @Autowired
    private ErrandMapper errandMapper;
    @Autowired
    private ErrandWebSocketController webSocketController;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private UserRepository userRepository;
    public ErrandDTO CreateErrand(ErrandDTO errandDTO){
         Errand errand = errandMapper.toEntity(errandDTO);
         errand.setStatus("Available");
         errand = errandRepo.save(errand);
         ErrandDTO errandDTO1 = errandMapper.toDTO(errand);
         webSocketController.broadcastNewErrand(errandDTO1);
         return errandDTO1;
    }

    public List<ErrandDTO> showAcceptedErrand1(Long userid){
        return errandRepo.findByRunner_IdAndStatus(userid,"Completed")
                .stream().map(errand -> errandMapper.toDTO((Errand) errand))
                .toList();
    }

    public List<ErrandDTO> showAcceptedErrand(Long userid){
        return errandRepo.findByCustomer_IdAndStatus(userid,"Completed")
                .stream().map(errand -> errandMapper.toDTO((Errand) errand))
                .toList();
    }

    @Transactional
    public ErrandResponseDTO AcceptingTask(Long id, User user){
        Errand errand = errandRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Errand Not found"));
        errand.setStatus("Accepted");

        errand.setRunner(user);

        UserProfile userProfile = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(()->new RuntimeException("userprofile not found"));

        ErrandDTO errandDTO = errandMapper.toDTO(errand);

        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);

        ErrandResponseDTO errandResponseDTO = new ErrandResponseDTO();

        errandResponseDTO.setErrandDTO(errandDTO);
        errandResponseDTO.setUserProfileDTO(userProfileDTO);

        User user1 = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        webSocketController.notifyErrandAccepted(user1.getUsername()
                ,userProfileDTO.getName(),
                errandDTO.getDescription());

        return errandResponseDTO;
    }
    @Transactional
    public ErrandDTO CancelTask(Long id,User user){
        Errand errand = errandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Errand not found"));

        errand.setStatus("Available");

        errand.setRunner(null);

        errandRepo.save(errand);

        ErrandDTO errandDTO = errandMapper.toDTO(errand);

        webSocketController.broadcastNewErrand(errandDTO);

        return errandDTO;
    }

    @Transactional
    public String taskCompleted(Long userprofileId,Long helperProfileId,Long errandId){
        UserProfile helperProfile = userProfileRepo.findById(helperProfileId)
                .orElseThrow(()-> new RuntimeException("not found "));

        UserProfile userProfile = userProfileRepo.findById(userprofileId)
                .orElseThrow(()->new RuntimeException("not found"));

        Errand errand = errandRepo.findById(errandId)
                .orElseThrow(()->new RuntimeException("not found"));

        errand.setStatus("Completed");
        userProfile.setTaskPosted(userProfile.getTaskPosted() + 1);
        helperProfile.setTaskAccepted(helperProfile.getTaskAccepted()+1);
        helperProfile.setEarning(helperProfile.getEarning() + errand.getPrice());

        userProfileRepo.save(userProfile);
        userProfileRepo.save(helperProfile);
        errandRepo.save(errand);

        return "Success";
    }
}
