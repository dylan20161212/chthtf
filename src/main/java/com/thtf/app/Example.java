 package com.thtf.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
public class Example {

//	@RequestMapping("/")
//	String home() {
//		return "He6llo WoTTTdddddrlwd7yy8d5!Ut";
//	}

	public static void main(String[] args) throws Exception {
		//System.setProperty("spring.devtools.restart.enabled", "true");
		System.out.println("start project ...........!");
		SpringApplication.run(Example.class, args);
	}

}