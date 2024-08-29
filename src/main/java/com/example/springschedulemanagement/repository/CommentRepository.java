package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByScheduleId(Long scheduleId);

    boolean existsById(Long id);
}
