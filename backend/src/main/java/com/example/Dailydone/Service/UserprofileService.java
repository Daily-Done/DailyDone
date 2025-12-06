package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.EarningStatsDTO;
import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.*;
import com.example.Dailydone.External.OtpGeneRater;
import com.example.Dailydone.Mapper.UserProfileMapper;
import com.example.Dailydone.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserprofileService {
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private TempOTPRepo tempOTPRepo;
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private RatingUserRepo ratingUserRepo;
    @Autowired
    private EarningRecordRepository earningRecordRepository;
    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    @Transactional
    public String CreateProfile(UserProfileDTO userProfileDTO){
        Optional<UserProfile> userProfile = userProfileRepo
                .findByPhone(userProfileDTO.getPhone());
        if(userProfile.isPresent()){
            throw new RuntimeException("No. Already registered");
        }
        UserProfile userProfile1 = userProfileMapper.toEntity(userProfileDTO);
        String DateString = userProfile1.getAge();
        String[] parts = DateString.split("-");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        LocalDate birthDate = LocalDate.of( year,month ,day );
        int age = calculateAge(birthDate);
        String a = String.valueOf(age);

        userProfile1.setAge(a);
        userProfile1 = userProfileRepo.save(userProfile1);

        EarningRecord earningRecord = new EarningRecord();
        earningRecord.setUser(userProfile1);
        earningRecordRepository.save(earningRecord);

        return "Success";
    }

    public UserProfileDTO UpdateProfile(Long id,UserProfileDTO userProfileDTO){
        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(()->new RuntimeException("User not found"));
        userProfile.setName(userProfileDTO.getName());
        userProfile.setPhone(userProfileDTO.getPhone());
        userProfile = userProfileRepo.save(userProfile);
        return userProfileMapper.toDTO(userProfile);
    }

    public UserProfileDTO GetProfile(Long id) {
        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(() -> new RuntimeException("User id not found"));

        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);

        List<Rating> ratings = ratingRepo.findByUserProfile(userProfile);
        double avg = 0;
        if (!ratings.isEmpty()) {
            double sum = 0;
            for (Rating rating : ratings) {
                sum += rating.getRating();
            }
            avg = sum / ratings.size();
        }


        List<RatingForUser> ratingForUser = ratingUserRepo.findByUserProfile1(userProfile);
        double avg1 = 0;
        if (!ratingForUser.isEmpty()) {
            double sum1 = 0;
            for (RatingForUser r : ratingForUser) {
                sum1 += r.getRating();
            }
            avg1 = sum1 / ratingForUser.size();
        }
        System.out.println("****" + avg);
        System.out.println("****" + avg1);

        userProfile.setRating(avg);
        userProfile.setUserRating(avg1);
        userProfileRepo.save(userProfile);

        userProfileDTO.setRating(avg);
        userProfileDTO.setUserBehaviour(avg1);

        if (userProfileDTO.getTaskPosted() < 0) {
            userProfileDTO.setTaskPosted(0);
        }

        return userProfileDTO;
    }

    @Transactional
    public String Verify(TempOtp OTP,String phone){
        TempOtp tempOtp = tempOTPRepo.findByPhone(phone)
                .orElseThrow(()->new RuntimeException("not found"));

        String otp = tempOtp.getOTP();

        System.out.println("user otp : " + OTP);
        if(!tempOtp.getOTP().equals(otp)){
            throw new RuntimeException("otp is wrong");
        }

        tempOTPRepo.deleteById(tempOtp.getId());

        return "Successfully completed login";
    }

    public EarningStatsDTO getMoneyStats(Long id) {

        System.out.println("**********");
        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(()->new RuntimeException("error on finding new errand"));

        try {
            EarningStatsDTO earningStatsDTO = new EarningStatsDTO();

            Double daily = Optional.ofNullable(earningRecordRepository.getDailyEarnings(userProfile.getId())).orElse(0.0);
            Double weekly = Optional.ofNullable(earningRecordRepository.getWeeklyEarnings(userProfile.getId())).orElse(0.0);
            Double monthly = Optional.ofNullable(earningRecordRepository.getMonthlyEarnings(userProfile.getId())).orElse(0.0);
            Double total = Optional.ofNullable(earningRecordRepository.getTotalEarnings(userProfile.getId())).orElse(0.0);

            earningStatsDTO.setDailyEarnings(daily);
            earningStatsDTO.setWeeklyEarnings(weekly);
            earningStatsDTO.setMonthlyEarnings(monthly);
            earningStatsDTO.setTotalEarnings(total);

            return earningStatsDTO;
        } catch (Exception e) {
            System.err.println("error has occur on this getMoneyStats method");
            return new EarningStatsDTO(0.0, 0.0, 0.0, 0.0); // fallback default
        }
    }
}