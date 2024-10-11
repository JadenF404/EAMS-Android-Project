package com.group25.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.group25.db.service")
public class EamsDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EamsDatabaseApplication.class, args);
	}

}
