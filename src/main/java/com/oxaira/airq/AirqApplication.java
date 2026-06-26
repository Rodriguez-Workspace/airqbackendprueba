package com.oxaira.airq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AirqApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirqApplication.class, args);
	}

}
