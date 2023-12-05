package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;



import com.univ.lille.copainsderoute.platine.entity.User;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTOs {

    public UserResponseDTOs(User user, List<Integer> participatedEvent_id) {
        this.login = user.getLogin();
        this.numberEventsParticipated = user.getNumberEventsParticipated();
        this.numberEventsCreated = user.getNumberEventsCreated();
        this.distanceTraveled = user.getDistanceTraveled();
        this.participatedEvent_id = participatedEvent_id;
    }
    
    public UserResponseDTOs(User user) {
        this.login = user.getLogin();
        this.numberEventsParticipated = user.getNumberEventsParticipated();
        this.numberEventsCreated = user.getNumberEventsCreated();
        this.distanceTraveled = user.getDistanceTraveled();
       // this.co2NotEmitted = user.getCo2NotEmitted();
    }


    private String login;

    private int numberEventsParticipated;

    private int numberEventsCreated;

    private int distanceTraveled;

    private int co2NotEmitted;

    private List<Integer> participatedEvent_id = new ArrayList<>(); 

   


//    private List <Friends> friends = new ArrayList<>();

}

