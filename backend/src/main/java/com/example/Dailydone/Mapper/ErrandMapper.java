package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.UserDTO;
import com.example.Dailydone.Entity.*;
import com.example.Dailydone.Repository.AddressRepo;
import com.example.Dailydone.Repository.CategoryRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import com.example.Dailydone.Repository.UserRepository;
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
        ErrandDTO dto = new ErrandDTO();
        dto.setId(errand.getId());
        dto.setDescription(errand.getDescription());
        dto.setPrice(errand.getPrice());
        dto.setStatus(errand.getStatus());
        dto.setCreatedAt(errand.getCreatedAt());
        dto.setUpdatedAt(errand.getUpdatedAt());

        // Extract IDs from relations
        User user = userRepository.findById(errand.getCustomer().getId())
                .orElseThrow(()->new RuntimeException("not found"));
        dto.setCustomerId(UserMapper.toDTO(user));

        User runner = userRepository.findById(errand.getCustomer().getId())
                .orElseThrow(()->new RuntimeException("not found"));
        dto.setRunnerId(UserMapper.toDTO(runner));

        Category category = categoryRepo.findById(errand.getCategory().getId())
                        .orElseThrow(()->new RuntimeException("Category not there"));
        dto.setCategoryId(CategoryMapper.toDTO(category));

        Address address = addressRepo.findById(errand.getPickupAddress().getId())
                        .orElseThrow(()->new RuntimeException("address not found"));
        dto.setPickupAddressId(addressMapper.toDTO(address));

        UserProfile userProfile = userProfileRepo.findById(errand.getUserProfile().getId())
                .orElseThrow(()->new RuntimeException("profile not found"));
        dto.setUserProfileId(userProfileMapper.toDTO(userProfile));

        if(errand.getHelperProfile().getId() != null) {
            UserProfile runnerProfile = userProfileRepo.findById(errand.getHelperProfile().getId())
                    .orElseThrow(() -> new RuntimeException("profile not found"));
            dto.setUserProfileId(userProfileMapper.toDTO(runnerProfile));
        }
        return dto;
    }

    public Errand toEntity(ErrandDTO dto) {
        if (dto == null) {
            return null;
        }
        Errand errand = new Errand();
        errand.setDescription(dto.getDescription());
        errand.setPrice(dto.getPrice());
        errand.setStatus(dto.getStatus());
        errand.setUpdatedAt(dto.getUpdatedAt());

        // Set only IDs for related entities
        if (dto.getCustomerId() != null) {
            User customer = userRepository.findById(dto.getCustomerId().getId())
                            .orElseThrow(() -> new RuntimeException("customer didn't exist"));
            errand.setCustomer(customer);
        }
        if (dto.getRunnerId() != null) {
            User runner = userRepository.findById(dto.getRunnerId().getId())
                            .orElseThrow(()->new RuntimeException("runner id didn't exist"));
            errand.setRunner(runner);
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId().getId())
                            .orElseThrow(() -> new RuntimeException("category didn't exist"));
            errand.setCategory(category);
        }
        if (dto.getPickupAddressId() != null) {
            Address pickup = addressRepo.findById(dto.getPickupAddressId().getId())
                            .orElseThrow(() -> new RuntimeException("address not found"));
            errand.setPickupAddress(pickup);
        }
        if (dto.getUserProfileId() != null){
            UserProfile userProfile = userProfileRepo.findById(dto.getUserProfileId().getId())
                    .orElseThrow(()->new RuntimeException("Userprofile not found"));

            errand.setUserProfile(userProfile);
        }

        if (dto.getHelperProfileId() != null){
            UserProfile helperProfile = userProfileRepo.findById(dto.getHelperProfileId().getId())
                    .orElseThrow(()->new RuntimeException("helperProfile not found"));

            errand.setUserProfile(helperProfile);
        }

        return errand;
    }
}
