package com.rxproject.rosbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class RosbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(RosbankApplication.class, args);
	}

}
