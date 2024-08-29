package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.schedule WHERE c.schedule.id = :scheduleId")
    List<Comment> findByScheduleId(Long scheduleId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.schedule")
    List<Comment> findAll();

    boolean existsById(Long id);

}
