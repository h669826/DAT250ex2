package com.example.demo.bootstrap;

import com.example.demo.model.User;
import com.example.demo.model.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DemoData implements ApplicationRunner {
    private final UserRepo userRepo;

    @Override
    public void run(ApplicationArguments args) {
        var email = "demo@example.com";
        userRepo.findByEmail(email).orElseGet(() -> {
            var u = new User();
            u.setUsername("demo");
            u.setEmail(email);
            return userRepo.save(u);
        });
    }
}