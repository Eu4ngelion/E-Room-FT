package com.eroomft.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ERoomAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ERoomAPIApplication.class, args);
    }
}