package com.example.Dailydone.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class FirebaseAuthController {

    @PostMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest tokenRequest) {
        try {
            String idToken = tokenRequest.getIdToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            String uid = decodedToken.getUid();
            Object phoneNumber = decodedToken.getClaims().get("phone_number");

            System.out.println("✅ Verified Firebase user UID: " + uid);
            System.out.println("📞 Phone number: " + phoneNumber);

            return ResponseEntity.ok("Token verified successfully for " + phoneNumber);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Invalid or expired token: " + e.getMessage());
        }
    }

    // helper class to map request body
    @Setter
    @Getter
    public static class TokenRequest {
        private String idToken;
    }
}