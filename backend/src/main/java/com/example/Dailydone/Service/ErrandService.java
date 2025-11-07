package com.example.Dailydone.Service;

import com.example.Dailydone.Controller.ErrandWebSocketController;
import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.Rating;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Mapper.UserProfileMapper;
import com.example.Dailydone.Repository.ErrandRepo;
import com.example.Dailydone.Repository.RatingRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import com.example.Dailydone.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ErrandService {
    @Autowired
    private RatingRepo ratingRepo;
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
    public ErrandDTO AcceptingTask(Long id, User user) {
        System.out.println("🚝 accepting task has been called");

        UserProfile helperProfile = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Helper profile not found"));

        Errand errand = errandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Errand not found"));

        errand.setStatus("Accepted");
        errand.setRunner(user);
        errand.setHelperProfile(helperProfile);

        System.out.println("✨ helper id: " + user.getId());

        errandRepo.saveAndFlush(errand);
        System.out.println("🩻🩻🩻🩻🩻🩻🩻🩻");
        errand = errandRepo.findById(errand.getId())
                .orElseThrow(() -> new RuntimeException("Failed to reload updated errand"));
        System.out.println("🧊🧊🧊🧊🧊🧊🧊🧊🧊🧊🧊🧊");
        ErrandDTO errandDTO = errandMapper.toDTO(errand);
        System.out.println("🧬🧬🧬🧬");
        System.out.println(errandDTO.getHelperProfileId().toString());
        System.out.println(" Task accepted successfully for helper: " + helperProfile.getId());
        return errandDTO;
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

    public UserProfileDTO getHelperProfile(Long id){

        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(()->new RuntimeException("User id not found"));

        System.out.println("got userProfile Rating "
                + userProfile.getRating() + " : "
                + userProfile.getRatingUser());

        return userProfileMapper.toDTO(userProfile);
    }

}
