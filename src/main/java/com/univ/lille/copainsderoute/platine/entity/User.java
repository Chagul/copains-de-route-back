package com.univ.lille.copainsderoute.platine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;


@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, name = "login")
    private String login;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "number_events_participated")
    private int numberEventsParticipated;

    @Column(name = "number_events_created")
    private int numberEventsCreated;

    @Column(name = "distance_traveled")
    private int distanceTraveled;

    @Column(name = "co2_not_emitted")
    private int co2_not_emitted;

    @OneToMany(mappedBy = "user1")
    private List <Friends> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private List<Event> participatedEvent = new ArrayList<>();
    

    

    // TODO add profile image

}





