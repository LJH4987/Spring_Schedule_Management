package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.WeatherDTO;

public interface WeatherService {
    WeatherDTO fetchTodayWeather();
}