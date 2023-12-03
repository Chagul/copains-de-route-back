package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import com.univ.lille.copainsderoute.platine.entity.Friends;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTOs {

    public int id;
    
    private String login;

    private String email;

    private String password;

    private int numberEventsParticipated;

    private int numberEventsCreated;

    private int distanceTraveled;

    private int co2_not_emitted;

    private Set<Friends> friends;
    
}
