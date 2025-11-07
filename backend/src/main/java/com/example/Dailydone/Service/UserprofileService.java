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
    private SmsAPI smsAPI;
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

        // --- Average for main rating ---
        List<Rating> ratings = ratingRepo.findByUserProfile(userProfile);
        double avg = 0;
        if (!ratings.isEmpty()) {
            double sum = 0;
            for (Rating rating : ratings) {
                sum += rating.getRating();
            }
            avg = sum / ratings.size();
        }

        // --- Average for user behaviour rating ---
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
        // --- Save updated values ---
        userProfile.setRating(avg);
        userProfile.setUserRating(avg1);
        userProfileRepo.save(userProfile);

        // --- Set DTO for response ---
        userProfileDTO.setRating(avg);
        userProfileDTO.setUserBehaviour(avg1);

        if (userProfileDTO.getTaskPosted() < 0) {
            userProfileDTO.setTaskPosted(0);
        }

        return userProfileDTO;
    }

    @Transactional
    public String sendOTP(String phone){
        TempOtp tempOtp = new TempOtp();
        String OTP = OtpGeneRater.generateOtp4();
        tempOtp.setOTP(OTP);

        System.out.println("this method has been called");

        tempOtp.setExpiry(LocalDateTime.now().plusMinutes(3));
        tempOtp.setPhone(phone);
        tempOtp = tempOTPRepo.save(tempOtp);

        smsAPI.sendOtpSms(tempOtp.getPhone(), tempOtp.getOTP());

        return "saved";
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

        System.out.println("this otp is verified called");

        tempOTPRepo.deleteById(tempOtp.getId());

        return "Successfully completed login";
    }

    public EarningStatsDTO getMoneyStats(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Step 1: Fetch the user profile safely
        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + id));

        // Step 2: Check if an earning record is linked
        if (userProfile.getEarningRecord() == null) {
            throw new RuntimeException("No earning record linked with user profile ID: " + userProfile.getId());
        }

        // Step 3: Fetch earning record from DB
        EarningRecord earningRecord = earningRecordRepository.findById(userProfile.getEarningRecord().getId())
                .orElseThrow(() -> new RuntimeException("Earning record not found in DB for profile ID: " + userProfile.getId()));

        // Step 4: Build response safely
        EarningStatsDTO earningStatsDTO = new EarningStatsDTO();

        try {
            earningStatsDTO.setDailyEarnings(
                    Optional.ofNullable(earningRecordRepository.getDailyEarnings(userProfile.getId())).orElse(0.0)
            );
            earningStatsDTO.setWeeklyEarnings(
                    Optional.ofNullable(earningRecordRepository.getWeeklyEarnings(userProfile.getId())).orElse(0.0)
            );
            earningStatsDTO.setMonthlyEarnings(
                    Optional.ofNullable(earningRecordRepository.getMonthlyEarnings(userProfile.getId())).orElse(0.0)
            );
            earningStatsDTO.setTotalEarnings(
                    Optional.ofNullable(earningRecordRepository.getTotalEarnings(userProfile.getId())).orElse(0.0)
            );
        } catch (Exception e) {
            // Catch unexpected DB or logic errors
            System.err.println("Error while fetching earnings for user ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to calculate earnings stats. Please try again later.");
        }

        return earningStatsDTO;
    }
}