package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.model.User;
import com.example.demo.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImp(UserRepo userRepo) {
        this.userRepo = userRepo;
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
    public void deleteUser(UUID uid) {
        userRepo.deleteById(uid);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }
}

