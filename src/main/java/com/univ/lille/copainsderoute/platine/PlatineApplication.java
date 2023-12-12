package com.univ.lille.copainsderoute.platine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PlatineApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatineApplication.class, args);
	}

}
