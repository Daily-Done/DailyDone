package com.example.Dailydone.Controller;

import com.example.Dailydone.DTO.AnalyticsResponse;
import com.example.Dailydone.Service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getDashboard() {
        log.info("Fetching analytics dashboard...");
        AnalyticsResponse response = analyticsService.getDashboard();
        return ResponseEntity.ok(response);
    }
}