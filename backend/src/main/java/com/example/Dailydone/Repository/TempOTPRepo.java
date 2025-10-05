package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.TempOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempOTPRepo extends JpaRepository<TempOtp, Long> {
    Optional<TempOtp> findByPhone(String phone);
}
