package com.univ.lille.copainsderoute.platine.service;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.CommentRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.CommentResponseDTOs;
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

    public CommentResponseDTOs createComment(CommentRequestDTOs commentRequestDTOs) throws RuntimeException {

        
        Comment comment = new Comment();

        comment.setSubmissionTime(LocalDateTime.now());
        comment.setContent(commentRequestDTOs.getContent());
        comment.setLikes(0);

        Event event = eventRepository.findById(commentRequestDTOs.getEvent()).orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findByLogin(commentRequestDTOs.getUserWhoCommented());

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        comment.setEvent(event);
        comment.setUserWhoCommented(commentRequestDTOs.getUserWhoCommented());

        List<Comment> comments = event.getComments();
        comments.add(comment);

        commentRepository.save(comment);

        event.setComments(comments);
        eventRepository.save(event);

        return new CommentResponseDTOs(comment);


    }

    public List<Comment> getAllCommentsByEvent(int id) {
        List<Comment> comments = commentRepository.findAllByEventId(id);
        return comments;
    }

    public Comment updateComment(CommentRequestDTOs commentRequestDTOs, int id) throws RuntimeException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (commentRequestDTOs.getContent() != null){
            comment.setContent(commentRequestDTOs.getContent());
        }
        if (commentRequestDTOs.getLikes() != 0) {
            comment.setLikes(comment.getLikes()+1);
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(int id) throws RuntimeException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
