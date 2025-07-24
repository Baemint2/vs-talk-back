package com.moz1mozi.vstalkbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VsTalkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VsTalkBackendApplication.class, args);
    }

}
