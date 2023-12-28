package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;



import com.univ.lille.copainsderoute.platine.entity.User;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTOs {

    private static final String USER_PATH = "/users/";
    private static final String PROFILE_PIC_PATH = "/profilePic";

    public UserResponseDTOs(User user, boolean withProfilePic) {
        this.login = user.getLogin();
        this.numberEventsParticipated = user.getNumberEventsParticipated();
        this.numberEventsCreated = user.getNumberEventsCreated();
        this.distanceTraveled = user.getDistanceTraveled();
        this.co2NotEmitted = user.getCo2_not_emitted();
        if(withProfilePic) {
            this.profilePicLocation = USER_PATH.concat(String.valueOf(user.getId())).concat(PROFILE_PIC_PATH);
        }
    }

    private String login;

    private int numberEventsParticipated;

    private int numberEventsCreated;

    private int distanceTraveled;

    private int co2NotEmitted;

    private String profilePicLocation;

//    private List <Friends> friends = new ArrayList<>();

}

