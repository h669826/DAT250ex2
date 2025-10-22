package com.example.demo.Services;

import com.example.demo.dto.CreateUserDto;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void addUser(User user);
    User getUser(UUID uid);
    Optional<User> getUserByEmail(String email);
    void deleteUser(UUID uid);
    List<User> getUsers();
    User create(String username, String email);
}
