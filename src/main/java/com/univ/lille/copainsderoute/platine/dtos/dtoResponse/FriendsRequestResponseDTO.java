package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendsRequestResponseDTO {

    private int id;
    private String sender;
    private String added;
    private String senderProfilePicLocation;
    private String addedProfilePicLocation;
    private LocalDateTime submissionTime;
    private LocalDateTime acceptedTime;
    private FriendRequestStatus status;

    public FriendsRequestResponseDTO(Friends friendRequest, String senderProfilePicLocation, String addedProfilePicLocation) {
        this.id = friendRequest.getId();
        this.sender = friendRequest.getSender().getLogin();
        this.added = friendRequest.getAdded().getLogin();
        this.senderProfilePicLocation = senderProfilePicLocation;
        this.addedProfilePicLocation = addedProfilePicLocation;
        this.submissionTime = friendRequest.getSubmissionTime();
        this.acceptedTime = friendRequest.getAcceptedTime();
        this.status = friendRequest.getStatus();
    }
}
