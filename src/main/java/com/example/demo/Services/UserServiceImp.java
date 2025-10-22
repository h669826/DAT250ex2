package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.model.User;
import com.example.demo.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;
    private final Environment env;

    @Autowired
    public UserServiceImp(UserRepo userRepo, Environment env) {
        this.userRepo = userRepo;
        this.env = env;
    }

    @Override
    public void addUser(User user) {
        userRepo.save(user);
    }

    @Override
    public User getUser(UUID uid) {
        return userRepo.findById(uid).orElse(null);
    }

    @Override
    public Optional<User> getUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    @Override
    public void deleteUser(UUID uid) {
        userRepo.deleteById(uid);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public User create(String username, String email) {
        // dev-only toggle; keep OFF in tests/prod
        boolean allowAutofill = env.acceptsProfiles(Profiles.of("local","dev"));

        String u = (username == null ? "" : username).trim();
        String e = (email == null ? "" : email).trim().toLowerCase();

        if (u.isBlank()) {
            if (!allowAutofill) throw new IllegalArgumentException("username is required");
            u = "user-" + UUID.randomUUID().toString().substring(0,8);
        }
        if (e.isBlank()) {
            if (!allowAutofill) throw new IllegalArgumentException("email is required");
            e = "autogen+" + UUID.randomUUID().toString().substring(0,8) + "@example.invalid";
        }

        // ensure uniqueness (with a few retries)
        for (int i = 0; i < 3 && userRepo.existsByUsername(u); i++) {
            if (!allowAutofill) throw new IllegalStateException("username already taken");
            u = u + "-" + (i + 2);
        }
        for (int i = 0; i < 3 && userRepo.existsByEmail(e); i++) {
            if (!allowAutofill) throw new IllegalStateException("email already taken");
            e = "autogen+" + UUID.randomUUID().toString().substring(0,8) + "@example.invalid";
        }

        User user = new User();
        user.setUsername(u);
        user.setEmail(e);
        return userRepo.save(user);
    }
}

