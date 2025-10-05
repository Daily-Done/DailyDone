package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.UserProfileDTO;
import com.example.Dailydone.Entity.TempOtp;
import com.example.Dailydone.Entity.UserProfile;
import com.example.Dailydone.External.OtpGeneRater;
import com.example.Dailydone.Mapper.UserProfileMapper;
import com.example.Dailydone.Repository.TempOTPRepo;
import com.example.Dailydone.Repository.UserProfileRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserprofileService {
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private TempOTPRepo tempOTPRepo;

    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public UserProfileDTO CreateProfile(UserProfileDTO userProfileDTO){
        Optional<UserProfile> userProfile = userProfileRepo
                .findByPhone(userProfileDTO.getPhone());
        if(userProfile.isPresent()){
            throw new RuntimeException("No. Already registered");
        }
        UserProfile userProfile1 = userProfileMapper.toEntity(userProfileDTO);
        String DateString = userProfile1.getAge();
        String[] parts = DateString.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        LocalDate birthDate = LocalDate.of( year,month ,day );
        int age = calculateAge(birthDate);
        String a = String.valueOf(age);

        userProfile1.setAge(a);
        userProfileRepo.save(userProfile1);
        return userProfileMapper.toDTO(userProfile1);
    }

    @CacheEvict(value = "userprofile",key = "#id")
    public UserProfileDTO UpdateProfile(Long id,UserProfileDTO userProfileDTO){
        UserProfile userProfile = userProfileRepo.findByUser_Id(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        userProfile.setProfileImage(userProfileDTO.getProfileImage());
        userProfile.setName(userProfileDTO.getName());
        userProfile.setPhone(userProfileDTO.getPhone());
        userProfile = userProfileRepo.save(userProfile);
        return userProfileMapper.toDTO(userProfile);
    }

    @Cacheable("userprofile")
    public UserProfileDTO GetProfile(Long id){
        return userProfileMapper.toDTO(userProfileRepo.findByUser_Id(id)
                .orElseThrow(()->new RuntimeException("User id not found")));
    }

    public String sendOTP(String phone){
        TempOtp tempOtp = new TempOtp();
        String OTP = OtpGeneRater.generateOtp4();
        tempOtp.setOTP(OTP);

        System.out.println("this method has been called");

        tempOtp.setExpiry(LocalDateTime.now().plusMinutes(3));
        tempOtp.setPhone(phone);
        tempOtp = tempOTPRepo.save(tempOtp);
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
}
