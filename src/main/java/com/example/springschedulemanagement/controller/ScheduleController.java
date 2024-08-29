package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationUtil;
import com.example.springschedulemanagement.dto.ScheduleDTO;
import com.example.springschedulemanagement.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JwtAuthorizationUtil jwtAuthorizationUtil;

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody ScheduleDTO scheduleDTO) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);
        ScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
        return ResponseEntity.created(URI.create("/schedules/" + createdSchedule.getId())).body(createdSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody ScheduleDTO scheduleDTO) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(id, scheduleDTO);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);
        return scheduleService.getScheduleById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ScheduleDTO>>> getAllSchedules(@RequestHeader(value = "Authorization", required = false) String token, @PageableDefault(size = 10) Pageable pageable, PagedResourcesAssembler<ScheduleDTO> assembler) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);
        Page<ScheduleDTO> schedules = scheduleService.getAllSchedules(pageable);

        PagedModel<EntityModel<ScheduleDTO>> pagedModel = assembler.toModel(schedules, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ScheduleController.class).getAllSchedules(token, pageable, assembler)).withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }
}
