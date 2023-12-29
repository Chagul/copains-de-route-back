package com.univ.lille.copainsderoute.platine.entity;

import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "added_id", nullable = false)
    private User added;

    @Column(name = "submission_time")
    private LocalDateTime submissionTime;

    @Column(name = "accepted_time")
    private LocalDateTime acceptedTime;

    private FriendRequestStatus status;
}
