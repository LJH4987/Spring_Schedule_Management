package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.UserScheduleDTO;
import com.example.springschedulemanagement.entity.Schedule;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.entity.UserSchedule;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.resource.ScheduleNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.UserNotFoundException;
import com.example.springschedulemanagement.exception.custom.schedule.UserScheduleConflictException;
import com.example.springschedulemanagement.repository.UserRepository;
import com.example.springschedulemanagement.repository.ScheduleRepository;
import com.example.springschedulemanagement.repository.UserScheduleRepository;
import com.example.springschedulemanagement.service.UserScheduleService;
import com.example.springschedulemanagement.service.mapper.UserScheduleMapper;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserScheduleServiceImpl implements UserScheduleService {

    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public UserScheduleDTO assignUserToSchedule(Long userId, Long scheduleId) {
        try {
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "사용자를 일정에 할당 중입니다. 유저 ID: {}, 일정 ID: {}", userId, scheduleId);

            validateUserScheduleConflict(userId, scheduleId);
            User user = findUserById(userId);
            Schedule schedule = findScheduleById(scheduleId);

            UserSchedule userSchedule = new UserSchedule();
            userSchedule.setUser(user);
            userSchedule.setSchedule(schedule);

            UserSchedule savedUserSchedule = userScheduleRepository.save(userSchedule);
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "사용자가 일정에 성공적으로 할당되었습니다. 유저 ID: {}, 일정 ID: {}", userId, scheduleId);

            return UserScheduleMapper.toDTO(savedUserSchedule);

        } catch (UserNotFoundException | ScheduleNotFoundException | UserScheduleConflictException e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "사용자를 일정에 할당하는 중 오류가 발생했습니다. 유저 ID: {}, 일정 ID: {}", userId, scheduleId, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 유저 ID {}와 일정 ID {}에 대한 사용자 할당 중 문제가 발생했습니다.", userId, scheduleId, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 유저에 대한 사용자 할당 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public UserScheduleDTO updateUserSchedule(Long id, UserScheduleDTO userScheduleDTO) {
        try {
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "할당된 일정을 수정 중입니다. ID: {}", id);

            UserSchedule userSchedule = findUserScheduleById(id);

            userSchedule.setUser(findUserById(userScheduleDTO.getUserId()));
            userSchedule.setSchedule(findScheduleById(userScheduleDTO.getScheduleId()));
            userSchedule.setUpdatedDate(LocalDateTime.now());

            UserSchedule updatedUserSchedule = userScheduleRepository.save(userSchedule);
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "할당된 일정이 성공적으로 수정되었습니다. ID: {}", id);

            return UserScheduleMapper.toDTO(updatedUserSchedule);

        } catch (UserNotFoundException | ScheduleNotFoundException e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "할당된 일정 수정 중 오류가 발생했습니다. ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. ID {}에 대한 UserSchedule 수정 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 할당된 일정에 대한 수정 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public void deleteUserSchedule(Long id) {
        try {
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "할당된 일정을 삭제 중입니다. ID: {}", id);

            UserSchedule userSchedule = findUserScheduleById(id);
            userScheduleRepository.delete(userSchedule);

            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "할당된 일정이 성공적으로 삭제되었습니다. ID: {}", id);

        } catch (ScheduleNotFoundException e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "할당된 일정 삭제 중 오류가 발생했습니다. ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. ID {}에 대한 할당된 일정 삭제 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 할당된 일정에 대한 삭제 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public Optional<UserScheduleDTO> getUserScheduleById(Long id) {
        try {
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "할당된 일정을 조회 중입니다. ID: {}", id);

            return userScheduleRepository.findById(id)
                    .map(UserScheduleMapper::toDTO)
                    .or(() -> {
                        ServiceLoggingUtil.logWarn(UserScheduleServiceImpl.class, "해당 ID를 가진 할당된 일정을 찾을 수 없습니다: {}", id);
                        return Optional.empty();
                    });

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 할당된 일정 ID {}에 대한 조회 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 할당된 일정에 대한 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public List<UserScheduleDTO> getAllUserSchedules() {
        try {
            ServiceLoggingUtil.logInfo(UserScheduleServiceImpl.class, "모든 UserSchedule을 조회 중입니다.");

            return userScheduleRepository.findAll().stream()
                    .map(UserScheduleMapper::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 모든 할당된 일정 조회 중 문제가 발생했습니다.", e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 모든 할당된 일정 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID " + userId + "를 가진 사용자를 찾을 수 없습니다."));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("해당 일정을 찾을 수 없습니다."));
    }

    private UserSchedule findUserScheduleById(Long id) {
        return userScheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("해당 ID를 가진 할당된 일정을 찾을 수 없습니다: " + id));
    }

    private void validateUserScheduleConflict(Long userId, Long scheduleId) {
        if (userScheduleRepository.existsByUserIdAndScheduleId(userId, scheduleId)) {
            throw new UserScheduleConflictException("유저 ID " + userId + "가 이미 일정 ID " + scheduleId + "에 할당되어 있습니다.");
        }
    }
}
