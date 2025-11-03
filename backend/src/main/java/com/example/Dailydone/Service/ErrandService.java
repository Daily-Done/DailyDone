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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ErrandService {
    @Autowired
    private EmailService emailService;
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
    @Transactional
    public void CreateErrand(ErrandDTO errandDTO){
         Errand errand = errandMapper.toEntity(errandDTO);

         UserProfile userProfile = userProfileRepo
                 .findByUser_Id(errand.getCustomer().getId())
                         .orElseThrow(() -> new RuntimeException("userprofile not found"));
         userProfile.setTaskPosted(userProfile.getTaskPosted() + 1);

         userProfileRepo.save(userProfile);
         errand.setStatus("Available");
         errand = errandRepo.save(errand);
         // webSocketController.broadcastNewErrand(errandDTO1);
         errandMapper.toDTO(errand);
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

    public List<ErrandDTO> showAllUserErrand(Long id){
        List<Errand> errands = errandRepo.findByCustomer_Id(id)
                .orElseThrow(()-> new RuntimeException("customer id not found"));
        return (errands.stream().map(errandMapper::toDTO).toList());
    }

    public List<ErrandDTO> showALlErrands(Long id,int page,int sizes){
        Pageable pageable = PageRequest.of(page, sizes); // no sorting

        Page<Errand> errandsPage = errandRepo.findAllByStatusAndCustomer_IdNot("Available", id, pageable);

        return errandsPage.getContent()
                .stream()
                .map(errandMapper::toDTO)
                .toList();
    }

    @Transactional
    public ErrandResponseDTO AcceptingTask(Long id, User user){
        System.out.println("🚝🚝🚝 accepting task has been called");

        UserProfile userProfile1 = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(()->new RuntimeException("not found"));

        Errand errand = errandRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Errand Not found"));
        errand.setStatus("Accepted");

        errand.setRunner(user);

        errand.setHelperProfile(userProfile1);

        System.out.println("✨✨✨ helper id: " + user.getId());

        UserProfile userProfile = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(()->new RuntimeException("userprofile not found"));

        ErrandDTO errandDTO = errandMapper.toDTO(errand);

        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);

        errandRepo.save(errand);

        ErrandResponseDTO errandResponseDTO = new ErrandResponseDTO();

        errandResponseDTO.setErrandDTO(errandDTO);
        errandResponseDTO.setUserProfileDTO(userProfileDTO);

        //User user1 = userRepository.findById(id)
          //      .orElseThrow(()->new RuntimeException("User not found"));

        //webSocketController.notifyErrandAccepted(user1.getUsername()
          //      ,userProfileDTO.getName(),
            //    errandDTO.getDescription());

        return errandResponseDTO;
    }
    @Transactional
    public ErrandDTO CancelTask(Long id,User user){
        System.out.println("this cancel helper is called ⚗️⚗️⚗️🛢️🛢️🩻🩻💉💉🩺🩺🧬🧬🔬🔬");
        System.out.println("🩻🩻💉💉💉😽😽👽👽🧬🧬🧬");
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

    @Transactional
    public String deleted(Long id,Long userid){

        UserProfile userProfile = userProfileRepo.findByUser_Id(userid)
                .orElseThrow(()->new RuntimeException("not found"));

        if(userProfile.getTaskPosted() <= 0){
            userProfile.setTaskPosted(0);
        }else {
            userProfile.setTaskPosted(userProfile.getTaskPosted() - 1);
        }
        userProfileRepo.save(userProfile);

        errandRepo.deleteById(id);

        return "errand has been deleted";

    }

    public List<ErrandDTO> helperTask(User user){
        System.out.println(user.getId());
        List<Errand> errand = errandRepo.findByRunner_IdAndStatusNot(user.getId(), "Available")
                .orElseThrow(()-> new RuntimeException("not found"));

        return (errand.stream().map(errandMapper::toDTO).toList());

    }

    public List<ErrandDTO> GetByCategories(int page ,int size,Long CustomerId,Long CatId){
        Pageable pageable = PageRequest.of(page, size); // no sorting

        Page<Errand> errandpage = errandRepo.findAllByStatusAndCustomer_IdNotAndCategory_Id(
                "Available",
                CustomerId,
                CatId,
                pageable
        );

        return errandpage.getContent()
                .stream()
                .map(errand -> errandMapper.toDTO(errand))
                .toList();
    }

    @Scheduled(fixedRate = 3600000)
    public void expireOldErrands() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusHours(24);

        Optional<List<Errand>> optionalErrands =
                errandRepo.findByStatusAndCreatedAtBefore("Available", expiryThreshold);

        if (optionalErrands.isPresent()) {
            List<Errand> errands = optionalErrands.get();
            errands.forEach(e -> e.setStatus("Expired"));

            for (Errand errand : errands) {
                User user = errand.getCustomer();
                if (user != null && user.getEmail() != null) {
                    try {
                        emailService.sendExpiryMessage(user.getEmail());
                        System.out.println("📧 Sent expiry email to: " + user.getEmail());
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        System.err.println("❌ Failed to send email to " + user.getEmail() + ": " + e.getMessage());
                    }
                }
            }

            errandRepo.saveAll(errands);
            System.out.println("✅ Auto-expired errands: " + errands.size());
        } else {
            System.out.println("ℹ️ No errands found to expire at this time.");
        }
    }

}
