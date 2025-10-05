package com.example.Dailydone.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("otp@dailydone.com"); // your email
        message.setSubject("Your DailyDone OTP");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }
}
