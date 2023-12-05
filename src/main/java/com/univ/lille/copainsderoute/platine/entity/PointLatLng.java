package com.univ.lille.copainsderoute.platine.entity;

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
@Table(name = "points")
public class PointLatLng {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int rank;
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "event_id")
        @JsonIgnore

 // Assurez-vous que le nom de la colonne correspond à votre modèle de base de données
    private Event event;

    

}




