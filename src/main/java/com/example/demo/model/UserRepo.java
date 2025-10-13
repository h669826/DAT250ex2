package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {}
