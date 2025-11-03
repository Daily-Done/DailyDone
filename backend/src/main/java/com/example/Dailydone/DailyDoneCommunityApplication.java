package com.example.Dailydone;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class DailyDoneCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyDoneCommunityApplication.class, args);
	}

}
