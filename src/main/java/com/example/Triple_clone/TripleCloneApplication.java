package com.example.Triple_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TripleCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripleCloneApplication.class, args);
	}

}
