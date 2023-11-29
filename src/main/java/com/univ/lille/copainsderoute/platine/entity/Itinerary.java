package com.univ.lille.copainsderoute.platine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class Itinerary {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int rank;
    private Double latitude;
    private Double longitude;

    private int event; // l'id de l'event

    

}
                