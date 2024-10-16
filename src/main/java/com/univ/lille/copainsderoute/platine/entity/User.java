package com.univ.lille.copainsderoute.platine.entity;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.UserRegisterRequestDTOs;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @OneToMany(mappedBy = "sender")
    private List<Friends> sentFriends = new ArrayList<>();

    @OneToMany(mappedBy = "added")
    private List<Friends> addedFriends = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private List<Event> participatedEvent = new ArrayList<>();

    public static User getUserFromDTO(UserRegisterRequestDTOs userRequestDTO) {
        User user = new User();
        user.setLogin(userRequestDTO.getLogin());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }
}





