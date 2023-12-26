package com.univ.lille.copainsderoute.platine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



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
    @ManyToOne
    private User promoter;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PointLatLng> steps = new ArrayList<>();
    

    @ManyToMany
    @JoinTable(
        name = "user_event",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @OneToMany (mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "max_participants")
    private int maxParticipants;

    @Column(name = "start_day")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

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

    private int distance;

    @Column(columnDefinition = "TEXT")
    private String route;   

    private String startAddress;

    private String endAddress;

    public void updateFromDTO(EventRequestDTOs eventRequestDTO) {
        if (eventRequestDTO.getName() != null) {
            this.setName(eventRequestDTO.getName());
        }
        if (eventRequestDTO.getDescription() != null) {
            this.setDescription(eventRequestDTO.getDescription());
        }
        if (eventRequestDTO.getVisibility() != null) {
            this.setVisibility(eventRequestDTO.getVisibility());
        }
        if (eventRequestDTO.getMaxParticipants() != 0) {
            this.setMaxParticipants(eventRequestDTO.getMaxParticipants());
        }
        if (eventRequestDTO.getStartDate() != null) {
            this.setStartDate(eventRequestDTO.getStartDate());
        }
        if (eventRequestDTO.getStartTime() != null) {
            this.setStartTime(eventRequestDTO.getStartTime());
        }
        if (eventRequestDTO.getRoadType1() != null) {
            this.setRoadType1(eventRequestDTO.getRoadType1());
        }
        if (eventRequestDTO.getRoadType2() != null) {
            this.setRoadType2(eventRequestDTO.getRoadType2());
        }
        if (eventRequestDTO.getRoadType3() != null) {
            this.setRoadType3(eventRequestDTO.getRoadType3());
        }
        if (eventRequestDTO.getBikeType1() != null) {
            this.setBikeType1(eventRequestDTO.getBikeType1());
        }
        if (eventRequestDTO.getBikeType2() != null) {
            this.setBikeType2(eventRequestDTO.getBikeType2());
        }
    }

    public static Event getEventFromDTO(EventRequestDTOs eventRequestDTO, User promoter) {
        Event evt = new Event();
        evt.setName(eventRequestDTO.getName());
        evt.setDescription(eventRequestDTO.getDescription());
        evt.setVisibility(eventRequestDTO.getVisibility());
        evt.setMaxParticipants(eventRequestDTO.getMaxParticipants());
        evt.setStartDate(eventRequestDTO.getStartDate());
        evt.setStartTime(eventRequestDTO.getStartTime());
        evt.setRoadType1(eventRequestDTO.getRoadType1());
        evt.setRoadType2(eventRequestDTO.getRoadType2());
        evt.setRoadType3(eventRequestDTO.getRoadType3());
        evt.setBikeType1(eventRequestDTO.getBikeType1());
        evt.setBikeType2(eventRequestDTO.getBikeType2());
        evt.setStartAddress(eventRequestDTO.getStartAddress());
        evt.setEndAddress(eventRequestDTO.getEndAddress());

        evt.setPromoter(promoter);

        evt.setRoute(eventRequestDTO.getRoute());
        evt.setDistance(eventRequestDTO.getDistance());
        return evt;
    }

    public boolean isUserInParticipants(User u) {
        return this.getParticipants().contains(u);
    }
}







