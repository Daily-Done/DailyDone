package com.example.Dailydone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DailyDoneCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyDoneCommunityApplication.class, args);
	}

}
