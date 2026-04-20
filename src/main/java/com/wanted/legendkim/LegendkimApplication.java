package com.wanted.legendkim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LegendkimApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegendkimApplication.class, args);
    }

}
