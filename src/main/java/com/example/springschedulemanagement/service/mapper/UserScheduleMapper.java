package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.UserScheduleDTO;
import com.example.springschedulemanagement.entity.UserSchedule;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;

public class UserScheduleMapper {

    public static UserScheduleDTO toDTO(UserSchedule userSchedule) {
        try {
            if (userSchedule == null) {
                throw new BaseException("유효하지 않은 UserSchedule 엔티티입니다.");
            }

            UserScheduleDTO userScheduleDTO = new UserScheduleDTO();
            userScheduleDTO.setId(userSchedule.getId());
            userScheduleDTO.setUserId(userSchedule.getUser().getId());
            userScheduleDTO.setScheduleId(userSchedule.getSchedule().getId());
            userScheduleDTO.setUserName(userSchedule.getUser().getName());
            userScheduleDTO.setScheduleTitle(userSchedule.getSchedule().getTitle());
            userScheduleDTO.setCreatedDate(userSchedule.getCreatedDate());
            userScheduleDTO.setUpdatedDate(userSchedule.getUpdatedDate());

            ServiceLoggingUtil.logDebug(UserScheduleMapper.class, "UserSchedule 엔티티를 UserScheduleDTO로 변환했습니다. UserSchedule ID: {}", userSchedule.getId());
            return userScheduleDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleMapper.class, "UserSchedule 엔티티를 UserScheduleDTO로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("UserSchedule 엔티티를 UserScheduleDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

}
