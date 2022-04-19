package com.ahdark.code.link;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkApplication.class, args);
    }
}
