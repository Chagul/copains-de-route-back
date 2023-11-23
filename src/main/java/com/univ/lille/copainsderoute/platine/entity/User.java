package com.univ.lille.copainsderoute.platine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;

import com.univ.lille.copainsderoute.platine.dtos.UserRequestDTOs;

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
    private Set<Friends> friends;

    // TODO add profile image

    public User(UserRequestDTOs userRequestDTOs)    {

        this.login = userRequestDTOs.getLogin();

        this.email = userRequestDTOs.getEmail();

        this.password = userRequestDTOs.getPassword();

        this.numberEventsParticipated= userRequestDTOs.getNumberEventsParticipated();

        this.numberEventsCreated= userRequestDTOs.getNumberEventsCreated();

        this.distanceTraveled = userRequestDTOs.getDistanceTraveled();

        this.co2_not_emitted = userRequestDTOs.getCo2_not_emitted();

        this.friends  = userRequestDTOs.getFriends();

    }
}
