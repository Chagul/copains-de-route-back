package com.univ.lille.copainsderoute.platine.service;
import com.univ.lille.copainsderoute.platine.dtos.CommentRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.repository.CommentRepository;
import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

 /** */   private CommentRepository commentRepository;

    private UserRepository userRepository;

    private EventRepository eventRepository;

    public Comment createComment(CommentRequestDTOs commentRequestDTOs) throws Exception {
        Comment comment = new Comment();

        comment.setSubmissionTime(LocalDateTime.now());
        comment.setContent(commentRequestDTOs.getContent());
        comment.setLikes(commentRequestDTOs.getLikes());

        Event event = eventRepository.findById(commentRequestDTOs.getEvent()).orElseThrow(() -> new Exception("Event not found"));
        User user = userRepository.findById(commentRequestDTOs.getUserWhoCommented()).orElseThrow(() -> new Exception("User not found"));

        comment.setEvent(event);
        comment.setUserWhoCommented(user);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsByEvent(int id) {
        List<Comment> comments = commentRepository.findAllByEvent_Id(id);
        return comments;
    }

    public Comment updateComment(CommentRequestDTOs commentRequestDTOs, int id) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new Exception("Comment not found"));

        comment.setContent(commentRequestDTOs.getContent());
        comment.setLikes(commentRequestDTOs.getLikes());

        return commentRepository.save(comment);
    }

    public void deleteComment(int id) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new Exception("Comment not found"));
        commentRepository.delete(comment);
    }
}
