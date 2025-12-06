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
   private AddressRepo addressRepo;

}
