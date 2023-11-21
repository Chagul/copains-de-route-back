package com.univ.lille.copainsderoute.platine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;
    private String email;
    private String password;

    @Column(name = "number_events_participated")
    private int numberEventsParticipated;

    @Column(name = "number_events_created")
    private int numberEventsCreated;

    @Column(name = "distance_traveled")
    private int distanceTraveled;

    @Column(name = "co2_not_emitted")
    private int co2_not_emitted;

    // TODO add profile image
}
