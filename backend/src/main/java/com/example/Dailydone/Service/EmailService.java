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

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    public void sendOtp(String toEmail, String otp) {
        OkHttpClient client = new OkHttpClient();

        String json = """
        {
          "sender": {"name": "DailyDone", "email": "dailydonehelp@gmail.com"},
          "to": [{"email": "%s"}],
          "subject": "Your DailyDone OTP",
          "htmlContent": "<p>Your OTP is <b>%s</b></p>"
        }
        """.formatted(toEmail, otp);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
                .url("https://api.brevo.com/v3/smtp/email")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("api-key", brevoApiKey)
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Brevo response code: " + response.code());
            if (!response.isSuccessful()) {
                System.err.println("Brevo failed: " + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
