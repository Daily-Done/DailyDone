package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.Address;
import com.example.Dailydone.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {
    Optional<Address> findByUser_Id(Long userId);
}
