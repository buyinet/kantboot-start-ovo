package com.kantboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaAuditing
@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class KantbootStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(KantbootStartApplication.class, args);
    }

}
