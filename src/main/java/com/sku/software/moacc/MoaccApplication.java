package com.sku.software.moacc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoaccApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoaccApplication.class, args);
	}

}
