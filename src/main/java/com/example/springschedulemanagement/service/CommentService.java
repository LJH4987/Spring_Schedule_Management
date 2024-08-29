package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.CommentDTO;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    Optional<CommentDTO> getCommentById(Long id);
    CommentDTO updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
    List<CommentDTO> getAllComments();
    List<CommentDTO> getAllCommentsForSchedule(Long scheduleId);
}