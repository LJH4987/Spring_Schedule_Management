package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.CommentDTO;
import com.example.springschedulemanagement.entity.Comment;
import com.example.springschedulemanagement.entity.Schedule;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;

public class CommentMapper {

    public static Comment toEntity(CommentDTO commentDTO) {
        try {
            if (commentDTO == null) {
                throw new BaseException("유효하지 않은 CommentDTO입니다.");
            }

            final Comment comment = getComment(commentDTO);

            ServiceLoggingUtil.logDebug(CommentMapper.class, "CommentDTO를 Comment 엔티티로 변환했습니다. 댓글 내용: {}", commentDTO.getContent());
            return comment;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentMapper.class, "CommentDTO를 Comment 엔티티로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("CommentDTO를 Comment 엔티티로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    private static Comment getComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedDate(commentDTO.getCreatedDate());
        comment.setUpdatedDate(commentDTO.getUpdatedDate());

        if (commentDTO.getUserId() != null) {
            User user = new User();
            user.setId(commentDTO.getUserId());
            comment.setUser(user);
        }

        if (commentDTO.getScheduleId() != null) {
            Schedule schedule = new Schedule();
            schedule.setId(commentDTO.getScheduleId());
            comment.setSchedule(schedule);
        }
        return comment;
    }

    public static CommentDTO toDTO(Comment comment) {
        try {
            if (comment == null) {
                throw new BaseException("유효하지 않은 Comment 엔티티입니다.");
            }

            final CommentDTO commentDTO = getCommentDTO(comment);

            ServiceLoggingUtil.logDebug(CommentMapper.class, "Comment 엔티티를 CommentDTO로 변환했습니다. 댓글 ID: {}", comment.getId());
            return commentDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(CommentMapper.class, "Comment 엔티티를 CommentDTO로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("Comment 엔티티를 CommentDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    private static CommentDTO getCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setScheduleId(comment.getSchedule().getId());
        commentDTO.setCreatedDate(comment.getCreatedDate());
        commentDTO.setUpdatedDate(comment.getUpdatedDate());
        commentDTO.setUserName(comment.getUser().getName());
        commentDTO.setScheduleTitle(comment.getSchedule().getTitle());
        return commentDTO;
    }
}