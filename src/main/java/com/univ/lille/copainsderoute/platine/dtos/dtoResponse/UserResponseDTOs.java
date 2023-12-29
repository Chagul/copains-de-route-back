package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;



import com.univ.lille.copainsderoute.platine.entity.User;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTOs {

    public UserResponseDTOs(User user, String profilePicLocation) {
        this.login = user.getLogin();
        this.numberEventsParticipated = user.getNumberEventsParticipated();
        this.numberEventsCreated = user.getNumberEventsCreated();
        this.distanceTraveled = user.getDistanceTraveled();
        this.co2NotEmitted = user.getCo2_not_emitted();
        this.profilePicLocation = profilePicLocation;
        this.sentFriends = user.getSentFriends().stream().map(FriendsRequestResponseDTO::new).toList();
        this.addedFriends = user.getAddedFriends().stream().map(FriendsRequestResponseDTO::new).toList();
    }

    private String login;

    private int numberEventsParticipated;

    private int numberEventsCreated;

    private int distanceTraveled;

    private int co2NotEmitted;

    private String profilePicLocation;

    private List<FriendsRequestResponseDTO> sentFriends;

    private List<FriendsRequestResponseDTO> addedFriends;

}

