package com.example.demo.Controllers;

import com.example.demo.Services.UserService;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
    @GetMapping("/{uid}")
    public User getUser(@PathVariable("uid") UUID uid) {
        return userService.getUser(uid);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }
    @DeleteMapping("/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("uid") UUID uid) {
        userService.deleteUser(uid);
    }
}
