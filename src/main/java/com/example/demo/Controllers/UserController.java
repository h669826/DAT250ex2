package com.example.demo.Controllers;

import com.example.demo.Services.UserService;
import com.example.demo.model.User;
import com.example.demo.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        },
        allowedHeaders = "*",
        methods = { GET, POST, PUT, PATCH, DELETE, OPTIONS },
        maxAge = 3600
)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @PostMapping
    public User create(@RequestBody User u) {
        u.setId(null);           // let JPA generate
        return userRepo.save(u);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
