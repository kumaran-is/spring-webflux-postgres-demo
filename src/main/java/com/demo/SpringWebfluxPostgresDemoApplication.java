package com.demo;

import org.springframework.boot.SpringApplication;
import reactor.blockhound.BlockHound;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebfluxPostgresDemoApplication {
	static {
        BlockHound.install();
    }
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxPostgresDemoApplication.class, args);
	}

}
