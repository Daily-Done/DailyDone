package com.example.Dailydone.Repository;

import com.example.Dailydone.Entity.Task;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT COUNT(t) " +
            "FROM Task t " +
            "WHERE FUNCTION('DATE', t.createdAt) = CURRENT_DATE")
    Long getTasksToday();

    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Task t " +
            "WHERE t.status = 'COMPLETED' " +
            "AND FUNCTION('DATE', t.createdAt) = CURRENT_DATE")
    Double getTodayMoney();

    @Query("SELECT t.category, COUNT(t) " +
            "FROM Task t " +
            "WHERE FUNCTION('DATE', t.createdAt) = CURRENT_DATE " +
            "GROUP BY t.category " +
            "ORDER BY COUNT(t) DESC")
    List<Object[]> getTodayCategoryCounts();

    @Query("SELECT t.helper.id, COUNT(t) " +
            "FROM Task t " +
            "WHERE t.status = 'COMPLETED' " +
            "GROUP BY t.helper.id " +
            "ORDER BY COUNT(t) DESC")
    List<Object[]> getCompletedTaskCountPerHelper();

    @Query("SELECT t.helper.id, COALESCE(SUM(t.amount), 0) " +
            "FROM Task t " +
            "WHERE t.status = 'COMPLETED' " +
            "GROUP BY t.helper.id " +
            "ORDER BY SUM(t.amount) DESC")
    List<Object[]> getHelperEarnings();

    @Query("SELECT t.helper.id, COALESCE(SUM(t.amount), 0) " +
            "FROM Task t " +
            "WHERE t.status = 'COMPLETED' " +
            "AND FUNCTION('DATE', t.createdAt) = CURRENT_DATE " +
            "GROUP BY t.helper.id " +
            "ORDER BY SUM(t.amount) DESC")
    List<Object[]> getTodayHelperEarnings();
}
