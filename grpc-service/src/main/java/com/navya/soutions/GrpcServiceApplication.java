package com.navya.soutions;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GrpcServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner ApplicationRunner(ProjectTestDataLoader projectTestDataLoader) {
        return args -> {
            // setup test data
            projectTestDataLoader.loadTestData();
        };
    }
}
