package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;

@Data   
@AllArgsConstructor
public class EventResponseDTOs {

    private String name;
    private String description;

    private String promoter;
    private int maxParticipants;

    private String startDate;
    private String startTime;

    private Visibility visibility;
    private RoadType roadType1;
    private RoadType roadType2;
    private RoadType roadType3;

    private BikeType bikeType1;
    private BikeType bikeType2;

    private String startPoint;
    private String endPoint;

    private List<Comment> comments = new ArrayList<>();
    private List<User> participants = new ArrayList<>();

    private int distance;
    public EventResponseDTOs(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.promoter = event.getPromoter().getLogin();
        this.maxParticipants = event.getMaxParticipants();
        this.startDate = event.getStartDate().toString();
        this.startTime = event.getStartTime().toString();
        this.visibility = event.getVisibility();
        this.roadType1 = event.getRoadType1();
        this.roadType2 = event.getRoadType2();
        this.roadType3 = event.getRoadType3();
        this.bikeType1 = event.getBikeType1();
        this.bikeType2 = event.getBikeType2();
        this.startPoint = event.getStartPoint().toString();
        this.endPoint = event.getEndPoint().toString();
        this.comments = event.getComments();
        this.participants = event.getParticipants();
        this.distance = event.getDistance();
    }

    

}
