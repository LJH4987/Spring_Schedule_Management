package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.ScheduleDTO;
import com.example.springschedulemanagement.entity.Schedule;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;

public class ScheduleMapper {

    public static Schedule toEntity(ScheduleDTO scheduleDTO) {
        try {
            if (scheduleDTO == null) {
                throw new BaseException("유효하지 않은 ScheduleDTO입니다.");
            }

            Schedule schedule = new Schedule();
            schedule.setId(scheduleDTO.getId());
            schedule.setTitle(scheduleDTO.getTitle());
            schedule.setDescription(scheduleDTO.getDescription());
            schedule.setCreatedDate(scheduleDTO.getCreatedDate());
            schedule.setUpdatedDate(scheduleDTO.getUpdatedDate());
            schedule.setWeather(scheduleDTO.getWeather());
            schedule.setDate(scheduleDTO.getDate());

            ServiceLoggingUtil.logDebug(ScheduleMapper.class, "ScheduleDTO를 Schedule 엔티티로 변환했습니다. 일정 제목: {}", scheduleDTO.getTitle());
            return schedule;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleMapper.class, "ScheduleDTO를 Schedule 엔티티로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("ScheduleDTO를 Schedule 엔티티로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    public static ScheduleDTO toDTO(Schedule schedule) {
        try {
            if (schedule == null) {
                throw new BaseException("유효하지 않은 Schedule 엔티티입니다.");
            }

            final ScheduleDTO scheduleDTO = getScheduleDTO(schedule);

            ServiceLoggingUtil.logDebug(ScheduleMapper.class, "Schedule 엔티티를 ScheduleDTO로 변환했습니다. 일정 제목: {}", schedule.getTitle());
            return scheduleDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleMapper.class, "Schedule 엔티티를 ScheduleDTO로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("Schedule 엔티티를 ScheduleDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    private static ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setUserId(schedule.getUser().getId());
        scheduleDTO.setUserName(schedule.getUser().getName());
        scheduleDTO.setTitle(schedule.getTitle());
        scheduleDTO.setCommentCount(schedule.getComments().size());
        scheduleDTO.setDescription(schedule.getDescription());
        scheduleDTO.setCreatedDate(schedule.getCreatedDate());
        scheduleDTO.setUpdatedDate(schedule.getUpdatedDate());
        scheduleDTO.setWeather(schedule.getWeather());
        scheduleDTO.setDate(schedule.getDate());
        return scheduleDTO;
    }
}