package com.example.springschedulemanagement.controller;

import com.example.springschedulemanagement.config.jwt.JwtAuthorizationFilter;
import com.example.springschedulemanagement.dto.CommentDTO;
import com.example.springschedulemanagement.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody CommentDTO commentDTO) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return ResponseEntity.created(URI.create("/comments/" + createdComment.getId())).body(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);
        return commentService.getCommentById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments(@RequestHeader(value = "Authorization", required = false) String token) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForSchedule(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long scheduleId) {

        jwtAuthorizationFilter.validateUserOrAdminToken(token);

        List<CommentDTO> comments = commentService.getAllCommentsForSchedule(scheduleId);
        return ResponseEntity.ok(comments);
    }

}
