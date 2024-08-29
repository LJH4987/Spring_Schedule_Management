package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationUtil;
import com.example.springschedulemanagement.dto.UserScheduleDTO;
import com.example.springschedulemanagement.service.UserScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/userschedules")
@RequiredArgsConstructor
public class UserScheduleController {

    private final UserScheduleService userScheduleService;
    private final JwtAuthorizationUtil jwtAuthorizationUtil;

    @Transactional
    @PostMapping("/assign")
    public ResponseEntity<UserScheduleDTO> assignUserToSchedule(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody UserScheduleDTO request) {

        jwtAuthorizationUtil.validateAdminToken(token);

        UserScheduleDTO assignedUserSchedule = userScheduleService.assignUserToSchedule(request.getUserId(), request.getScheduleId());
        return ResponseEntity.created(URI.create("/userschedules/" + assignedUserSchedule.getId())).body(assignedUserSchedule);
    }


    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSchedule(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateAdminToken(token);

        userScheduleService.deleteUserSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<UserScheduleDTO> updateUserSchedule(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody UserScheduleDTO userScheduleDTO) {

        jwtAuthorizationUtil.validateAdminToken(token);

        UserScheduleDTO updatedUserSchedule = userScheduleService.updateUserSchedule(id, userScheduleDTO);
        return ResponseEntity.ok(updatedUserSchedule);
    }


    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<UserScheduleDTO> getUserScheduleById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);

        return userScheduleService.getUserScheduleById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<UserScheduleDTO>> getAllUserSchedules(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationUtil.validateAdminToken(token);

        List<UserScheduleDTO> userSchedules = userScheduleService.getAllUserSchedules();
        return ResponseEntity.ok(userSchedules);
    }
}
