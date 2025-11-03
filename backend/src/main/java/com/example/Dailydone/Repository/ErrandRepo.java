package com.example.Dailydone.Repository;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ErrandRepo extends JpaRepository<Errand,Long> {
    Optional<List<Errand>> findByCustomer_IdAndStatus(Long user_id, String status);
    Optional<List<Errand>> findByRunner_IdAndStatus(Long user_id,String status);
    Page<Errand> findAllByStatusAndCustomer_IdNot(String status, Long customerId, Pageable pageable);
    Page<Errand> findAllByStatusAndCustomer_IdNotAndCategory_Id(
            String status,
            Long customerId,
            Long categoryId,
            Pageable pageable
    );
    Optional<List<Errand>> findByCustomer_Id(Long id);
    Optional<List<Errand>> findByRunner_IdAndStatusNot(Long runnerId, String status);
    Optional<List<Errand>> findByStatusAndCreatedAtBefore(String status, LocalDateTime time);

}
