package com.univ.lille.copainsderoute.platine.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private LocalDateTime submissionTime;

    private String userWhoCommented;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    
    private Event event;
    private int likes;

    private List<String> usersWhoLiked = new ArrayList<>();

    // TODO add image in comment (optional)
    


}
