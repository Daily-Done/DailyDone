package com.example.Dailydone.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import okhttp3.RequestBody;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKeyValue;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void sendExpiryMessage(String toEmail){
        try {

            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(apiKeyValue); // <-- from your @Value property

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail); // e.g. team@dailydone.in
            sender.setName(senderName);   // e.g. Dailydone


            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(toEmail);

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(to));
            email.setSubject("Task Expiry Message");
            email.setTextContent("No helper has accepted Your Task in last " +
                    "24 hours so we are expiring your task");

            apiInstance.sendTransacEmail(email);

            System.out.println("🐗🐗 Message Send Successfully " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Failed to send Message " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("👻👻 Message sent successfully to " + toEmail);
    }

    public void sendOtp(String toEmail, String otp) {


        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(apiKeyValue);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);

            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(toEmail);

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(to));
            email.setSubject("Your DailyDone OTP");
            email.setTextContent("Your OTP is: " + otp);

            apiInstance.sendTransacEmail(email);

            System.out.println("✅ OTP Email sent successfully to " + toEmail);

         } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("✅ OTP Email sent successfully to " + toEmail);
    }
}
