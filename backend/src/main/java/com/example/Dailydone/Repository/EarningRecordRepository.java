package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.EarningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarningRecordRepository extends JpaRepository<EarningRecord, Long> {
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM EarningRecord e WHERE e.user.id = :userId")
    Double getTotalEarnings(@Param("userId") Long userId);

    @Query(
            value = "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM earning_record " +
                    "WHERE user_id = :userId " +
                    "AND DATE_PART('month', earned_at) = DATE_PART('month', CURRENT_DATE) " +
                    "AND DATE_PART('year', earned_at) = DATE_PART('year', CURRENT_DATE)",
            nativeQuery = true
    )
    Double getMonthlyEarnings(@Param("userId") Long userId);

    @Query(
            value = "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM earning_record " +
                    "WHERE user_id = :userId " +
                    "AND earned_at >= date_trunc('week', CURRENT_DATE) " +
            "AND earned_at < date_trunc('week', CURRENT_DATE) + INTERVAL '7 day'",
             nativeQuery = true
            )
    Double getWeeklyEarnings(@Param("userId") Long userId);

    @Query(
            value = "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM earning_record " +
                    "WHERE user_id = :userId " +
                    "AND CAST(earned_at AS DATE) = CURRENT_DATE",
            nativeQuery = true
    )
    Double getDailyEarnings(@Param("userId") Long userId);
    EarningRecord findByUser_Id(Long userId);
}
