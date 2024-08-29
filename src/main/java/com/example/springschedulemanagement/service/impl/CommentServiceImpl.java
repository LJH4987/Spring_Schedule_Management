package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.CommentDTO;
import com.example.springschedulemanagement.dto.UserRoleDTO;
import com.example.springschedulemanagement.entity.Comment;
import com.example.springschedulemanagement.entity.Schedule;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.resource.CommentNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.ScheduleNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.UserNotFoundException;
import com.example.springschedulemanagement.repository.CommentRepository;
import com.example.springschedulemanagement.repository.ScheduleRepository;
import com.example.springschedulemanagement.repository.UserRepository;
import com.example.springschedulemanagement.service.CommentService;
import com.example.springschedulemanagement.service.mapper.CommentMapper;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        try {
            User user = findUserById(commentDTO.getUserId());
            Schedule schedule = findScheduleById(commentDTO.getScheduleId());

            Comment comment = CommentMapper.toEntity(commentDTO);
            comment.setUser(user);
            comment.setSchedule(schedule);
            comment = commentRepository.save(comment);

            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글이 성공적으로 생성되었습니다. 댓글 ID: {}", comment.getId());

            return CommentMapper.toDTO(comment);

        } catch (BaseException e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "댓글 생성에 실패했습니다. 유저 ID: {}, 스케줄 ID: {}", commentDTO.getUserId(), commentDTO.getScheduleId(), e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "예상치 못한 오류가 발생했습니다. 유저 ID {}와 스케줄 ID {}에 대한 댓글 생성 중 문제가 발생했습니다.", commentDTO.getUserId(), commentDTO.getScheduleId(), e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 댓글 생성 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        try {
            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글을 수정 중입니다. 댓글 ID: {}", id);

            Comment comment = findCommentById(id);
            comment.setContent(commentDTO.getContent());
            comment.setUpdatedDate(LocalDateTime.now());

            Comment updatedComment = commentRepository.save(comment);
            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글이 성공적으로 수정되었습니다. 댓글 ID: {}", id);

            return CommentMapper.toDTO(updatedComment);
        } catch (BaseException e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "댓글 수정에 실패했습니다. 댓글 ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "예상치 못한 오류가 발생했습니다. 댓글 ID {}에 대한 수정 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 댓글 수정 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        try {
            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글을 삭제 중입니다. 댓글 ID: {}", id);

            Comment comment = findCommentById(id);
            commentRepository.delete(comment);

            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글이 성공적으로 삭제되었습니다. 댓글 ID: {}", id);
        } catch (BaseException e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "댓글 삭제에 실패했습니다. 댓글 ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "예상치 못한 오류가 발생했습니다. 댓글 ID {}에 대한 삭제 중 문제가 발생했습니다.", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 댓글 삭제 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public Optional<CommentDTO> getCommentById(Long id) {
        ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "댓글을 조회 중입니다. 댓글 ID: {}", id);

        return commentRepository.findById(id)
                .map(CommentMapper::toDTO)
                .or(() -> {
                    ServiceLoggingUtil.logWarn(CommentServiceImpl.class, "해당 ID를 가진 댓글을 찾을 수 없습니다: {}", id);
                    return Optional.empty();
                });
    }

    @Override
    public List<CommentDTO> getAllComments() {
        try {
            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "모든 댓글을 조회 중입니다.");

            List<Comment> comments = commentRepository.findAll();
            return comments.stream()
                    .map(CommentMapper::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "모든 댓글 조회 중 오류가 발생했습니다.", e);
            throw new BaseException("모든 댓글 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }


    @Override
    public List<CommentDTO> getAllCommentsForSchedule(Long scheduleId) {
        try {
            ServiceLoggingUtil.logInfo(CommentServiceImpl.class, "스케줄 ID {}에 대한 모든 댓글을 조회 중입니다.", scheduleId);

            List<Comment> comments = commentRepository.findByScheduleId(scheduleId);
            return comments.stream()
                    .map(CommentMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentServiceImpl.class, "예상치 못한 오류가 발생했습니다. 스케줄 ID {}에 대한 모든 댓글 조회 중 문제가 발생했습니다.", scheduleId, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 스케줄에 대한 모든 댓글 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다. ID를 확인해주세요: " + userId));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("스케줄을 찾을 수 없습니다. ID를 확인해주세요: " + scheduleId));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 ID를 가진 댓글을 찾을 수 없습니다: " + commentId));
    }
}
