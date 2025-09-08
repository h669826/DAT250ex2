package com.example.demo.Services;

import com.example.demo.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(User user);
    User getUser(UUID uid);
    void deleteUser(UUID uid);
    List<User> getUsers();
}
