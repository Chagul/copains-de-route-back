package com.univ.lille.copainsderoute.platine.entity;

import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private Visibility visibility;

    @PrimaryKeyJoinColumn(name = "promoter_id")
    @OneToOne
    private User promoter;

    @OneToMany(mappedBy = "event")
    private Set<Itinerary> itineraryPoints;

    @Column(name = "max_participants")
    private int maxParticipants;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "road_type1")
    private RoadType roadType1;

    @Column(name = "road_type2")
    private RoadType roadType2;

    @Column(name = "road_type3")
    private RoadType roadType3;

    @Column(name = "bike_type1")
    private BikeType bikeType1;

    @Column(name = "bike_type2")
    private BikeType bikeType2;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    

}
