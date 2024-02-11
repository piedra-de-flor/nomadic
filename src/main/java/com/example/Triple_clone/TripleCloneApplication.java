package com.example.Triple_clone;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class TripleCloneApplication {
	public static void main(String[] args) {
		SpringApplication.run(TripleCloneApplication.class, args);
	}

/*	@Value("${test}")
	String password;*/

/*	@PostConstruct
	private void postConstruct() {
		System.out.println("My password is: " + password);
	}*/
}
