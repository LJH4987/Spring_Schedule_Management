package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationUtil;
import com.example.springschedulemanagement.dto.UserDTO;
import com.example.springschedulemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtAuthorizationUtil jwtAuthorizationUtil;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody UserDTO userDTO) {

        jwtAuthorizationUtil.validateAdminToken(token);

        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateAdminToken(token);

        return userService.getUserById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationUtil.validateAdminToken(token);

        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {

        jwtAuthorizationUtil.validateAdminToken(token);

        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateAdminToken(token);

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}