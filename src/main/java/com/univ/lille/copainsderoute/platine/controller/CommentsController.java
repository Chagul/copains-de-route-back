package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.exceptions.CommentNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.EventNotfoundException;
import com.univ.lille.copainsderoute.platine.exceptions.NoCommentException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.CommentResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("comments")
@AllArgsConstructor


public class CommentsController {

    private CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDTOs commentRequestDTOs) {
        int createdCommentId;
        try {
            createdCommentId = commentService.createComment(commentRequestDTOs);
        } catch (UserNotFoundException | EventNotfoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.created(URI.create("/comments/" + createdCommentId)).body(createdCommentId);
    }
    
   @GetMapping("{event_id}")
    public ResponseEntity<List<CommentResponseDTOs>> getAllCommentsByEvent(@PathVariable("event_id") int id) {
       List<CommentResponseDTOs> comments = null;
       try {
           comments = commentService.getAllCommentsByEvent(id);
       } catch (NoCommentException e) {
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.ok(comments);
    }
    
    @PatchMapping("{comment_id}")
    public ResponseEntity<Comment> updateComment(@RequestBody CommentRequestDTOs commentRequestDTOs, @PathVariable("comment_id") int id) {
        Comment updatedComment = null;
        try {
            updatedComment = commentService.updateComment(commentRequestDTOs, id);
        } catch (CommentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedComment);

    }

    @DeleteMapping("{comment_id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("comment_id") int id) {
        try {
            commentService.deleteComment(id);
        } catch (CommentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{comment_id}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable("comment_id") int id, HttpServletRequest request) {
        Comment likedComment = null;
        try {
            likedComment = commentService.likeComment(id, jwtUtil.getLogin(request));
        } catch (CommentNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(likedComment);
    }

    @PatchMapping("{comment_id}/unlike")
    public ResponseEntity<Comment> unlikeComment(@PathVariable("comment_id") int id, HttpServletRequest request) {
        Comment likedComment = null;
        try {
            likedComment = commentService.unlikeComment(id, jwtUtil.getLogin(request));
        } catch (CommentNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(likedComment);
    }
}
