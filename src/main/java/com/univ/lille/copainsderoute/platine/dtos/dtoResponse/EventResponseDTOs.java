package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import org.locationtech.jts.awt.PointShapeFactory.Point;

import java.util.ArrayList;

import com.univ.lille.copainsderoute.platine.entity.Comment;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.PointLatLng;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;

@Data   
@AllArgsConstructor
public class EventResponseDTOs {

    private int id;

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

    private String route;
    private String startAddress;
    private String endAddress;


    private List<CommentResponseDTOs> comments = new ArrayList<>();
    private List<UserResponseDTOs> participants = new ArrayList<>();
    private List<PointResponseDTOs> steps = new ArrayList<>();

    private int distance;



    public EventResponseDTOs(Event event) {
        this.id = event.getId();
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

        
        for (PointLatLng point : event.getSteps()){
            PointResponseDTOs pointResponseDTOs = new PointResponseDTOs(point);
            this.steps.add(pointResponseDTOs);
        }
        
        for (User user : event.getParticipants()){
            UserResponseDTOs userResponseDTOs = new UserResponseDTOs(user);
            this.participants.add(userResponseDTOs);
    }
        
        for (Comment comment : event.getComments()){
            CommentResponseDTOs commentResponseDTOs = new CommentResponseDTOs(comment);
            this.comments.add(commentResponseDTOs);
        }

        this.distance = event.getDistance();
        this.route = event.getRoute();
        this.startAddress = event.getStartAddress();
        this.endAddress = event.getEndAddress();
    }

    public boolean isFull() {
        return this.participants.size() == this.getMaxParticipants();
    }

}
