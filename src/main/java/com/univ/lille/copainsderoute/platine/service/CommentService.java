package com.univ.lille.copainsderoute.platine.service;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.CommentRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.CommentResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.exceptions.CommentNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.EventNotfoundException;
import com.univ.lille.copainsderoute.platine.exceptions.NoCommentException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
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

    public int createComment(CommentRequestDTOs commentRequestDTOs) throws UserNotFoundException, EventNotfoundException {
        Comment comment = new Comment();
        comment.setSubmissionTime(LocalDateTime.now());
        comment.setContent(commentRequestDTOs.getContent());
        comment.setLikes(0);

        Event event = eventRepository.findById(commentRequestDTOs.getEvent()).orElseThrow(EventNotfoundException::new);
        User user = userRepository.findByLogin(commentRequestDTOs.getUserWhoCommented()).orElseThrow(UserNotFoundException::new);

        comment.setEvent(event);
        comment.setUserWhoCommented(commentRequestDTOs.getUserWhoCommented());

        List<Comment> comments = event.getComments();
        comments.add(comment);
        comment = commentRepository.save(comment);
        event.setComments(comments);
        eventRepository.save(event);
        return comment.getId();
    }

    public List<CommentResponseDTOs> getAllCommentsByEvent(int id) throws NoCommentException {
        List<Comment> comments = commentRepository.findAllByEventId(id);
        if(comments.isEmpty()) {
            throw new NoCommentException();
        }
        return comments.stream().map(CommentResponseDTOs::new).toList();
    }

    public Comment updateComment(CommentRequestDTOs commentRequestDTOs, int id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        if (commentRequestDTOs.getContent() != null){
            comment.setContent(commentRequestDTOs.getContent());
        }
        if (commentRequestDTOs.getLikes() != 0) {
            comment.setLikes(comment.getLikes()+1);
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(int id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }

    public Comment likeComment (int id, String login) throws CommentNotFoundException, UserNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        comment.setLikes(comment.getLikes()+1);
        comment.getUsersWhoLiked().add(login);
        return commentRepository.save(comment);
    }

    public Comment unlikeComment (int id, String login) throws CommentNotFoundException, UserNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        comment.setLikes(comment.getLikes()-1);
        comment.getUsersWhoLiked().remove(login);
        return commentRepository.save(comment);
    }
}
