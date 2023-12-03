package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTOs {
    
    private String login;

    private String email;

    private int numberEventsParticipated;

    private int numberEventsCreated;

    private int distanceTraveled;

    private int co2_not_emitted;

//    private List <Friends> friends = new ArrayList<>();

}
