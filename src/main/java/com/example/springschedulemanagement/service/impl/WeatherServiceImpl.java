package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.WeatherDTO;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import com.example.springschedulemanagement.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String WEATHER_API_URL = "https://f-api.github.io/f-api/weather.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    @Override
    public WeatherDTO fetchTodayWeather() {
        try {
            String responseBody = fetchWeatherData();
            ServiceLoggingUtil.logDebug(WeatherServiceImpl.class, "API 원본 응답: {}", responseBody);

            List<WeatherDTO> weatherDTOList = parseWeatherData(responseBody);

            WeatherDTO todayWeather = findTodayWeather(weatherDTOList);
            ServiceLoggingUtil.logInfo(WeatherServiceImpl.class, "오늘의 날씨 데이터: 날짜 - {}, 날씨 - {}", todayWeather.getDate(), todayWeather.getWeather());

            return todayWeather;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(WeatherServiceImpl.class, "날씨 데이터를 가져오는 데 실패했습니다", e);
            return getDefaultWeather();
        }
    }

    private String fetchWeatherData() {
        ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_API_URL, String.class);
        return response.getBody();
    }

    private List<WeatherDTO> parseWeatherData(String responseBody) throws Exception {
        return objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, WeatherDTO.class)
        );
    }

    private WeatherDTO findTodayWeather(List<WeatherDTO> weatherDTOList) {
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return weatherDTOList.stream()
                .filter(weatherDTO -> weatherDTO.getDate().equals(currentDate))
                .findFirst()
                .orElse(new WeatherDTO(currentDate, "N/A"));
    }

    private WeatherDTO getDefaultWeather() {
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return new WeatherDTO(currentDate, "N/A");
    }
}
