package com.univ.lille.copainsderoute.platine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.dtos.CommentRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.service.CommentService;

import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("comments")
@AllArgsConstructor


public class CommentsController {

    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Comment> createComment (@RequestBody CommentRequestDTOs commentRequestDTOs) throws Exception {
        Comment createdComment = commentService.createComment(commentRequestDTOs);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);

    }
    
   @GetMapping("{event_id}")
    public ResponseEntity<List<Comment>> getAllCommentsByEvent (@PathVariable("event_id") int id) throws Exception {
        List<Comment> comments = commentService.getAllCommentsByEvent(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);


    }
    
    @PatchMapping("{comment_id}")
    public ResponseEntity<Comment> updateComment (@RequestBody CommentRequestDTOs commentRequestDTOs, @PathVariable("comment_id") int id) throws Exception {
        Comment updatedComment = commentService.updateComment(commentRequestDTOs, id);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);

    }

    @DeleteMapping("{comment_id}")
    public ResponseEntity<Void> deleteComment (@PathVariable("comment_id") int id) throws Exception {
        commentService.deleteComment(id);
        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}
