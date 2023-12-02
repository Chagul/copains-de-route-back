package com.univ.lille.copainsderoute.platine.entity;


import java.time.LocalDateTime;


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

    private int eventId;
    private int likes;

    // TODO add image in comment (optional)
    


}
