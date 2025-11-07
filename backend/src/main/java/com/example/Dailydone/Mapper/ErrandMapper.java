package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.*;
import com.example.Dailydone.Repository.AddressRepo;
import com.example.Dailydone.Repository.CategoryRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import com.example.Dailydone.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrandMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private UserProfileRepo userProfileRepo;

    public ErrandDTO toDTO(Errand errand) {
        if (errand == null) {
            return null;
        }
        System.out.println("this method is called");
        ErrandDTO dto = new ErrandDTO();
        dto.setId(errand.getId());
        dto.setDescription(errand.getDescription());
        dto.setPrice(errand.getPrice());
        dto.setStatus(errand.getStatus());
        dto.setCreatedAt(errand.getCreatedAt());
        dto.setUpdatedAt(errand.getUpdatedAt());
        dto.setUrgency(errand.getUrgency());
        // Extract IDs from relations
        User user = userRepository.findById(errand.getCustomer().getId())
                .orElseThrow(()->new RuntimeException("not found"));
        dto.setCustomerId(UserMapper.toDTO(user));

        if(errand.getCategory() != null && errand.getCategory().getId() != null) {

            Category category = categoryRepo.findById(errand.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not there"));
            dto.setCategoryId(CategoryMapper.toDTO(category));
        }
        if(errand.getPickupAddress() != null && errand.getPickupAddress().getId() != null) {
            Address address = addressRepo.findById(errand.getPickupAddress().getId())
                    .orElseThrow(() -> new RuntimeException("address not found"));
            dto.setPickupAddressId(addressMapper.toDTO(address));
        }

        if(errand.getUserProfile() != null && errand.getUserProfile().getId() != null) {
            System.out.println("type :" + errand.getUserProfile().getId());
            UserProfile userProfile = userProfileRepo.findById(errand.getUserProfile().getId())
                    .orElseThrow(() -> new RuntimeException("profile not found"));
            dto.setUserProfileId(userProfileMapper.toDTO(userProfile));
        }
        if(errand.getHelperProfile() != null && errand.getHelperProfile().getId() != null) {
            UserProfile runnerProfile = userProfileRepo.findById(errand.getHelperProfile().getId())
                    .orElseThrow(() -> new RuntimeException("profile not found"));
            dto.setHelperProfileId(userProfileMapper.toDTO(runnerProfile));
        }
        return dto;
    }

    @Transactional
    public Errand toEntity(ErrandDTO dto) {
        if (dto == null) {
            return null;
        }
        Errand errand = new Errand();
        errand.setDescription(dto.getDescription());
        errand.setPrice(dto.getPrice());
        errand.setStatus("Available");
        errand.setUpdatedAt(dto.getUpdatedAt());
        errand.setUrgency(dto.getUrgency());

        if (dto.getCustomerId() != null && dto.getCustomerId().getId() != null) {
            User customer = userRepository.findById(dto.getCustomerId().getId())
                            .orElseThrow(() -> new RuntimeException("customer didn't exist"));
            errand.setCustomer(customer);
            System.out.println("customer id is not null");
        }
        if (dto.getRunnerId() != null && dto.getRunnerId().getId() != null) {
            User runner = userRepository.findById(dto.getRunnerId().getId())
                            .orElseThrow(()->new RuntimeException("runner id didn't exist"));
            errand.setRunner(runner);
        }
        if (dto.getCategoryId() != null && dto.getCategoryId().getId() != null) {
            Category category1 = categoryRepo.findById(dto.getCategoryId().getId())
                            .orElseThrow(() -> new RuntimeException("category didn't exist"));
            errand.setCategory(category1);
        }
        if (dto.getPickupAddressId() != null) {
            Address address = addressMapper.toEntity(dto.getPickupAddressId());
            address = addressRepo.save(address);

            System.out.println("just save address inside my address repo");
            System.out.println("****************************************");

            Address pickup = addressRepo.findById(address.getId())
                            .orElseThrow(() -> new RuntimeException("address not found"));
            errand.setPickupAddress(pickup);
        }
            if(dto.getCustomerId() != null && dto.getCustomerId().getId() != null) {
                UserProfile userProfile = userProfileRepo
                        .findByUser_Id(dto.getCustomerId().getId())
                        .orElseThrow(() -> new RuntimeException("profile not found"));

                System.out.println("userprofile id is passed");

                UserProfile userProfile1 = userProfileRepo.findById(userProfile.getId())
                        .orElseThrow(() -> new RuntimeException("Userprofile not found"));

                errand.setUserProfile(userProfile1);
            }
        if (dto.getHelperProfileId() != null && dto.getHelperProfileId().getId() != null){

            UserProfile helperProfile = userProfileRepo.findById(dto.getHelperProfileId().getId())
                    .orElseThrow(()->new RuntimeException("helperProfile not found"));

            errand.setHelperProfile(helperProfile);
        }

        return errand;
    }
}
