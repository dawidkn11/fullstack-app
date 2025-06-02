package com.example.pasir_knapczyk_dawid.controller;


import com.example.pasir_knapczyk_dawid.dto.LoginDTO;
import com.example.pasir_knapczyk_dawid.dto.UserDTO;
import com.example.pasir_knapczyk_dawid.model.User;
import com.example.pasir_knapczyk_dawid.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            String token = userService.login(dto);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }


}
