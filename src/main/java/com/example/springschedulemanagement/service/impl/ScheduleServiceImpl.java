package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.ScheduleDTO;
import com.example.springschedulemanagement.dto.WeatherDTO;
import com.example.springschedulemanagement.entity.Schedule;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.resource.ScheduleNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.UserNotFoundException;
import com.example.springschedulemanagement.exception.custom.validation.InvalidDataAccessApiUsageException;
import com.example.springschedulemanagement.exception.custom.validation.InvalidPageSizeException;
import com.example.springschedulemanagement.repository.ScheduleRepository;
import com.example.springschedulemanagement.repository.UserRepository;
import com.example.springschedulemanagement.service.ScheduleService;
import com.example.springschedulemanagement.service.WeatherService;
import com.example.springschedulemanagement.service.mapper.ScheduleMapper;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    @Override
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        try {
            validateUserExistence(scheduleDTO.getUserId());

            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정을 생성 중입니다. 유저 ID: {}", scheduleDTO.getUserId());

            // 일정 저장
            Schedule schedule = ScheduleMapper.toEntity(scheduleDTO);
            User user = userRepository.findById(scheduleDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("해당 ID를 가진 유저를 찾을 수 없습니다: " + scheduleDTO.getUserId()));
            schedule.setUser(user);

            WeatherDTO todayWeather = weatherService.fetchTodayWeather();
            schedule.setWeather(todayWeather.getWeather());
            schedule.setDate(todayWeather.getDate());

            schedule = scheduleRepository.save(schedule);
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정이 성공적으로 생성되었습니다. 일정 ID: {}", schedule.getId());

            return ScheduleMapper.toDTO(schedule);

        } catch (UserNotFoundException e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "유저 ID {}를 찾을 수 없습니다.", scheduleDTO.getUserId(), e);
            throw new BaseException("일정 생성에 실패했습니다. 유저 ID " + scheduleDTO.getUserId() + "를 찾을 수 없습니다.", e);
        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 유저 ID {}와 관련된 일정 생성 중 문제가 발생했습니다.", scheduleDTO.getUserId(), e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 유저와 관련된 일정 생성 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        try {
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정을 수정 중입니다. 일정 ID: {}", id);

            Schedule schedule = scheduleRepository.findById(id)
                    .orElseThrow(() -> new ScheduleNotFoundException("해당 ID를 가진 일정을 찾을 수 없습니다: " + id));

            schedule.setTitle(scheduleDTO.getTitle());
            schedule.setDescription(scheduleDTO.getDescription());
            schedule.setUpdatedDate(LocalDateTime.now());

            Schedule updatedSchedule = scheduleRepository.save(schedule);
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정이 성공적으로 수정되었습니다. 일정 ID: {}", id);

            return ScheduleMapper.toDTO(updatedSchedule);
        } catch (ScheduleNotFoundException e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "일정 수정에 실패했습니다. 해당 ID를 가진 일정을 찾을 수 없습니다: {}", id, e);
            throw new BaseException("일정 수정에 실패했습니다. 일정을 찾을 수 없습니다.", e);
        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 일정 ID {}에 대한 수정 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 일정에 대한 수정 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        try {
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정을 삭제 중입니다. 일정 ID: {}", id);

            Schedule schedule = scheduleRepository.findById(id)
                    .orElseThrow(() -> new ScheduleNotFoundException("해당 ID를 가진 일정을 찾을 수 없습니다: " + id));

            scheduleRepository.delete(schedule);
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정이 성공적으로 삭제되었습니다. 일정 ID: {}", id);
        } catch (ScheduleNotFoundException e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "일정 삭제에 실패했습니다. 해당 ID를 가진 일정을 찾을 수 없습니다: {}", id, e);
            throw new BaseException("일정 삭제에 실패했습니다. 일정을 찾을 수 없습니다.", e);
        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 일정 ID {}에 대한 삭제 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 일정에 대한 삭제 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public Optional<ScheduleDTO> getScheduleById(Long id) {
        ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "일정을 조회 중입니다. 일정 ID: {}", id);

        return scheduleRepository.findById(id)
                .map(ScheduleMapper::toDTO)
                .or(() -> {
                    ServiceLoggingUtil.logWarn(ScheduleServiceImpl.class, "해당 ID를 가진 일정을 찾을 수 없습니다: {}", id);
                    return Optional.empty();
                });
    }

    @Override
    public Page<ScheduleDTO> getAllSchedules(@PageableDefault(size = 10) Pageable pageable) {
        final int MAX_PAGE_SIZE = 100;
        final int MAX_PAGE_NUMBER = Integer.MAX_VALUE / MAX_PAGE_SIZE;

        try {
            ServiceLoggingUtil.logInfo(ScheduleServiceImpl.class, "모든 일정을 조회 중입니다.");

            if (pageable.getPageSize() > MAX_PAGE_SIZE) {
                throw new InvalidPageSizeException("요청한 페이지 사이즈가 최대 허용 사이즈를 초과합니다.");
            }

            if (pageable.getPageNumber() > MAX_PAGE_NUMBER) {
                throw new InvalidDataAccessApiUsageException("페이지 번호가 너무 큽니다.");
            }

            Page<Schedule> schedulePage = scheduleRepository.findAll(pageable);

            if (pageable.getPageNumber() >= schedulePage.getTotalPages() && schedulePage.getTotalPages() > 0) {
                throw new InvalidDataAccessApiUsageException("요청한 페이지 번호가 총 페이지 수를 초과합니다.");
            }

            return schedulePage.map(ScheduleMapper::toDTO);
        } catch (InvalidPageSizeException | InvalidDataAccessApiUsageException e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "페이지 요청 또는 사이즈 오류: {}", pageable, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(ScheduleServiceImpl.class, "예상치 못한 오류가 발생했습니다. 모든 일정 조회 중 문제가 발생했습니다.", e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 모든 일정 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    private void validateUserExistence(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("유저 ID가 필요합니다.");
        } else if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("해당 ID를 가진 유저를 찾을 수 없습니다: " + userId);
        }
    }
}
