package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationUtil;
import com.example.springschedulemanagement.dto.WeatherDTO;
import com.example.springschedulemanagement.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final JwtAuthorizationUtil jwtAuthorizationUtil;

    @Transactional(readOnly = true)
    @GetMapping("/today")
    public ResponseEntity<WeatherDTO> fetchTodayWeather(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationUtil.validateUserOrAdminToken(token);

        WeatherDTO weatherDTO = weatherService.fetchTodayWeather();
        return ResponseEntity.ok(weatherDTO);
    }
}

