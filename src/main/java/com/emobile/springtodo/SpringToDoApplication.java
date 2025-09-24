package com.emobile.springtodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringToDoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringToDoApplication.class, args);
    }
}
