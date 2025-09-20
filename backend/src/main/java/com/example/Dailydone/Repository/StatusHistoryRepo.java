package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusHistoryRepo extends JpaRepository<StatusHistory,Long> {

}
