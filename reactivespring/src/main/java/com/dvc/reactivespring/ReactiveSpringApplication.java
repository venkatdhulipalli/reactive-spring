package com.dvc.reactivespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
//@EnableReactiveMongoRepositories
public class ReactiveSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSpringApplication.class, args);
	}

}
