package com.brainz.framework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@SpringBootApplication
public class WebframeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebframeworkApplication.class, args);
    }  
}    