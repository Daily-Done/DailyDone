package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Address;
import com.example.Dailydone.Entity.Category;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Repository.AddressRepo;
import com.example.Dailydone.Repository.CategoryRepo;
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
        dto.setCustomerId(errand.getCustomer() != null ? errand.getCustomer().getId() : null);
        dto.setRunnerId(errand.getRunner() != null ? errand.getRunner().getId() : null);
        dto.setCategoryId(errand.getCategory() != null ? errand.getCategory().getId() : null);
        dto.setPickupAddressId(errand.getPickupAddress() != null ? errand.getPickupAddress().getId() : null);
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
            User customer = userRepository.findById(dto.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("customer didn't exist"));
            errand.setCustomer(customer);
        }
        if (dto.getRunnerId() != null) {
            User runner = userRepository.findById(dto.getRunnerId())
                            .orElseThrow(()->new RuntimeException("runner id didn't exist"));
            errand.setRunner(runner);
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("category didn't exist"));
            errand.setCategory(category);
        }
        if (dto.getPickupAddressId() != null) {
            Address pickup = addressRepo.findById(dto.getPickupAddressId())
                            .orElseThrow(() -> new RuntimeException("address not found"));
            errand.setPickupAddress(pickup);
        }

        return errand;
    }
}
