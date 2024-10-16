package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import java.time.LocalDateTime;
import java.util.List;

import com.univ.lille.copainsderoute.platine.entity.Comment;

import lombok.Data;

@Data
public class CommentResponseDTOs {
   
    public CommentResponseDTOs(Comment comment) {

        this.id = comment.getId();
        this.login = comment.getUserWhoCommented();
        this.content = comment.getContent();
        this.date = comment.getSubmissionTime();
        this.likes = comment.getLikes();
        this.userLiked = comment.getUsersWhoLiked().contains(this.login);


    }
    private int id;
    private String login;
    private String content;
    private LocalDateTime date;
    private int likes;
    private boolean userLiked;

}
