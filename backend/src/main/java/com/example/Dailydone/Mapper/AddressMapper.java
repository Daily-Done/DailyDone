package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.AddressDTO;
import com.example.Dailydone.Entity.Address;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    @Autowired
    private UserRepository userRepository;
    public AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddress(address.getAddress());
        dto.setLocation(address.getLocation());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        if (address.getUser() != null) {
            dto.setUserId(UserMapper.toDTO(address.getUser()));
        }
        return dto;
    }

    // DTO → Entity
    public Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setAddress(dto.getAddress());
        address.setLocation(dto.getLocation());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        if(dto.getUserId() != null){
            User user = userRepository.findById(dto.getUserId().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            address.setUser(user);
        }// User object passed from service
        return address;
    }
}
