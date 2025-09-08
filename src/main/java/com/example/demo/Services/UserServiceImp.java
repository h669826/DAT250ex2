package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    private final DomainManager domainManager;

    @Autowired
    public UserServiceImp(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @Override
    public void addUser(User user) {
        domainManager.addUser(user);
    }

    @Override
    public User getUser(UUID uid) {
        return domainManager.getUser(uid);
    }

    @Override
    public void deleteUser(UUID uid) {
        domainManager.deleteUser(uid);
    }

    @Override
    public List<User> getUsers() {
        return domainManager.getUsers();
    }
}
