package com.example.demo;

import com.example.demo.model.UserRepo;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DomainApplication {

    private static final Logger log = LoggerFactory.getLogger(DomainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DomainApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(UserRepo users) {
        return args -> {
            if (users.count() == 0) {
                var u = new User();
                u.setName("Demo User");
                u.setEmail("demo@example.com");
                var saved = users.save(u);
                System.out.println("Seeded user id = " + saved.getId());
            }
        };
    }
}
