package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.DTO.UserProfileDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrandWebSocketController {

    private final SimpMessagingTemplate template;

    public ErrandWebSocketController(SimpMessagingTemplate template) {

        this.template = template;
    }

    // Broadcast new errand to all users
    public void broadcastNewErrand(ErrandDTO errand) {
        template.convertAndSend("/topic/errands/new", errand);
    }
    public void notifyErrandAccepted(String username,
                                     String errandDescription,
                                     String helperFullName) {
        String message = "Your errand '" +
                errandDescription +
                "' has been accepted by " + helperFullName;
        template.convertAndSendToUser(
                username,
                "/queue/errand-updates",
                message
        );
    }
}
