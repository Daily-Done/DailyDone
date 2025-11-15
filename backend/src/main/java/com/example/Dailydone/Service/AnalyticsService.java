package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.AnalyticsResponse;
import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.Mapper.UserMapper;
import com.example.Dailydone.Mapper.UserProfileMapper;
import com.example.Dailydone.Repository.RatingRepo;
import com.example.Dailydone.Repository.RatingUserRepo;
import com.example.Dailydone.Repository.TaskRepository;
import com.example.Dailydone.Repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private RatingUserRepo ratingUserRepo;

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileRepo userRepo;

    public AnalyticsResponse getDashboard() {

        AnalyticsResponse response = new AnalyticsResponse();

        Double todayMoney = taskRepo.getTodayMoney();

        response.setTotalMoneyExchangeToday(todayMoney == null ? 0.0 : todayMoney);

        List<Object[]> catData = taskRepo.getTodayCategoryCounts();

        if (!catData.isEmpty()) {
            response.setMostRequestedCategoryToday((String) catData.get(0)[0]);
            response.setCategoryCountToday((Long) catData.get(0)[1]);
        } else {
            response.setMostRequestedCategoryToday("None");
            response.setCategoryCountToday(0L);
        }

        Long tasksToday = taskRepo.getTasksToday();
        response.setTasksToday(tasksToday == null ? 0L : tasksToday);

        List<Object[]> helperData = taskRepo.getCompletedTaskCountPerHelper();
        List<UserProfile> topHelpers = new ArrayList<>();
        double bestScore = -1;

        for (Object[] row : helperData) {

            Long helperId = (Long) row[0];
            Long completedTasks = (Long) row[1];

            // ⭐ Correct average rating using your REAL entity
            Double avgRating = ratingRepo.getAverageRating(helperId);
            if (avgRating == null) avgRating = 0.0;

            double score = (avgRating * 20) + completedTasks;

            if (score > bestScore) {
                bestScore = score;
                topHelpers.clear();
                userRepo.findById(helperId).ifPresent(topHelpers::add);
            }
            else if (score == bestScore) {
                userRepo.findById(helperId).ifPresent(topHelpers::add);
            }
        }

        List<UserProfileDTO> topHelperDtos = new ArrayList<>();
        for (UserProfile helper : topHelpers) {
            UserProfileDTO dto = userProfileMapper.toDTO(helper);

            Double avgRating = ratingRepo.getAverageRating(helper.getId());
            if (avgRating == null) avgRating = 0.0;

            dto.setRating(avgRating);
            topHelperDtos.add(dto);
        }

        response.setTopHelpers(topHelperDtos);


        // ----------------------------------------------------
        // 5️⃣ Highest earning helpers TODAY only
        // ----------------------------------------------------
        List<Object[]> earnersToday = taskRepo.getTodayHelperEarnings();
        List<UserProfile> highestEarnersToday = new ArrayList<>();
        double bestEarningToday = -1;

        for (Object[] row : earnersToday) {

            Long helperId = (Long) row[0];
            Double earned = (Double) row[1];

            if (earned > bestEarningToday) {
                bestEarningToday = earned;
                highestEarnersToday.clear();
                userRepo.findById(helperId).ifPresent(highestEarnersToday::add);
            }
            else if (earned.equals(bestEarningToday)) {
                userRepo.findById(helperId).ifPresent(highestEarnersToday::add);
            }
        }

        // Convert to DTOs
        List<UserProfileDTO> highestTodayDTOs = new ArrayList<>();
        for (UserProfile user : highestEarnersToday) {
            UserProfileDTO dto = userProfileMapper.toDTO(user);

            Double avgRating = ratingRepo.getAverageRating(user.getId());
            if (avgRating == null) avgRating = 0.0;

            dto.setRating(avgRating);
            highestTodayDTOs.add(dto);
        }

        response.setHighestEarningHelpers(highestTodayDTOs);

        return response;
    }

    }
