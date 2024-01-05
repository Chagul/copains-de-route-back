package com.univ.lille.copainsderoute.platine.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "userEmail")
    private String user;

    private LocalDateTime expiryDate;

}
