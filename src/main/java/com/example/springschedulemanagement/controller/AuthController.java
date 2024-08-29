package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.dto.AuthUserDTO;
import com.example.springschedulemanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthUserDTO> register(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody AuthUserDTO authUserDTO) {


        AuthUserDTO registeredUser = authService.register(authUserDTO);
        return ResponseEntity.created(URI.create("/auth/register/" + registeredUser.getId())).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String hashPassword) {

        String token = authService.login(email, hashPassword);
        return ResponseEntity.ok(token);
    }
}
