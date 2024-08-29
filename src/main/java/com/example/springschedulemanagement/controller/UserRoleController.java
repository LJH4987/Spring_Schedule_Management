package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationUtil;
import com.example.springschedulemanagement.dto.UserRoleDTO;
import com.example.springschedulemanagement.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final JwtAuthorizationUtil jwtAuthorizationUtil;


    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getRoleById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateAdminToken(token);

        return userRoleService.getRoleById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllRoles(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationUtil.validateAdminToken(token);

        List<UserRoleDTO> roles = userRoleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> assignRole(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody UserRoleDTO userRoleDTO) {

        jwtAuthorizationUtil.validateAdminToken(token);

        UserRoleDTO updatedRole = userRoleService.assignRole(id, userRoleDTO);
        return ResponseEntity.ok(updatedRole);
    }

}

