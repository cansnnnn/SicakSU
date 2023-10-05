package com.sicaksu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.sicaksu")
@EnableMongoRepositories(basePackages = "com.sicaksu.repo")
public class SicakSuApplication {

	public static void main(String[] args) {
		SpringApplication.run(SicakSuApplication.class, args);
	}

}
