package com.capstonebe.capstonebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CapstoneBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneBeApplication.class, args);
    }

}
