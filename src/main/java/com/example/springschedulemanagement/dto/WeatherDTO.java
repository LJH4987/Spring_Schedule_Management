package com.example.springschedulemanagement.dto;

import lombok.Data;

@Data
public class WeatherDTO extends GenericDTO<Long> {
    private Long scheduleId;
    private Double temperature;
    private String weatherDescription;
}
