package com.example.Dailydone.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class SmsAPI {

    private final String API = "9N46iy1FQL2aeXfTgYuz5IEhKoksGpnvbJDOV8cBRPmwArUWqMAmi8I9uB7gV4CvyeZEHwDGMr1XbfUo";
    public void sendOtpSms(String phoneNumber, String otp) {
        try {
            String message = "Your DailyDone OTP is: " + otp;

            // ✅ JSON body required by Fast2SMS
            String requestBody = "{"
                    + "\"sender_id\":\"TXTIND\","
                    + "\"message\":\"" + message + "\","
                    + "\"language\":\"english\","
                    + "\"route\":\"v3\","
                    + "\"numbers\":\"" + phoneNumber + "\""
                    + "}";

            // ✅ Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("authorization", API.trim()); // ensure no spaces or newlines

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();

            // ✅ Use postForEntity (simpler & safer than exchange)
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://www.fast2sms.com/dev/bulkV2",
                    entity,
                    String.class
            );

            System.out.println("HTTP STATUS: " + response.getStatusCodeValue());

            if (response.getBody() != null) {
                System.out.println("✅ OTP sent successfully to " + phoneNumber);
                System.out.println("Response: " + response.getBody());
            } else {
                System.err.println("⚠️ No response body from Fast2SMS. Possible issues:");
                System.err.println("- Wrong or expired API key");
                System.err.println("- Missing DLT template");
                System.err.println("- Wrong content type or route");
            }

        } catch (Exception e) {
            System.err.println("❌ Exception while sending OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
