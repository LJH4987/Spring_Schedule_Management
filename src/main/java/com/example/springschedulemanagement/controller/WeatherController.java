package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationFilter;
import com.example.springschedulemanagement.dto.WeatherDTO;
import com.example.springschedulemanagement.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @GetMapping("/today")
    public ResponseEntity<WeatherDTO> fetchTodayWeather(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);

        WeatherDTO weatherDTO = weatherService.fetchTodayWeather();
        return ResponseEntity.ok(weatherDTO);
    }
}

