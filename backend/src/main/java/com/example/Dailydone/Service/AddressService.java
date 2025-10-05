package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.AddressDTO;
import com.example.Dailydone.DTO.GeoResponse;
import com.example.Dailydone.Entity.Address;
import com.example.Dailydone.Entity.User;
import com.example.Dailydone.Mapper.AddressMapper;
import com.example.Dailydone.Repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
   @Autowired
   private GeoLatLong geoLatLong;
   @Autowired
   private AddressRepo addressRepo;
    private final AddressMapper addressMapper;

    public AddressService(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }
    @CacheEvict(value = "address")
    public AddressDTO saveAddress(AddressDTO addressDTO) {
        Address address1 = addressMapper.toEntity(addressDTO);
        Optional<GeoResponse> geoResponse = geoLatLong.getCoordinates(address1.getAddress());

        if(geoResponse.isPresent()) {
            address1.setLatitude(geoResponse.get().getLatitude());
            address1.setLongitude(geoResponse.get().getLongitude());
        } else {
            throw new RuntimeException("Could not fetch coordinates for: " + address1.getAddress());
        }
        address1 = addressRepo.save(address1);
        return addressMapper.toDTO(address1);
    }

    @Cacheable("address")
    public AddressDTO getAddress(Long id){
        Address address = addressRepo.findByUser_Id(id)
                .orElseThrow(()->new RuntimeException("address not found"));
        return addressMapper.toDTO(address);
    }
}
