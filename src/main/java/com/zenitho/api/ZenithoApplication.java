package com.zenitho.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class ZenithoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZenithoApplication.class, args);
	}

}