package com.example.demo.Controllers;

import com.example.demo.Services.UserService;
import com.example.demo.dto.CreateUserDto;
import com.example.demo.model.User;
import com.example.demo.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    private final UserService userService;
    private final UserRepo userRepo;

    public UserController(UserService userService, UserRepo userRepo) { this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> create(@RequestBody CreateUserDto dto) {
        User u = userService.create(dto.username(), dto.email());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(u.getId())
                .toUri();
        return ResponseEntity.created(location).body(u); // 201 CREATED
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<User> list() {
        var all = userRepo.findAll();
        all.forEach(u -> { u.getCreated().size(); u.getVotes().size(); });
        return all;
    }
}
