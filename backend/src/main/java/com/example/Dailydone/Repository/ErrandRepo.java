package com.example.Dailydone.Repository;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ErrandRepo extends JpaRepository<Errand,Long> {
    Optional<List<Errand>> findByCustomer_IdAndStatus(Long user_id, String status);
    Optional<List<Errand>> findByRunner_IdAndStatus(Long user_id,String status);
}
