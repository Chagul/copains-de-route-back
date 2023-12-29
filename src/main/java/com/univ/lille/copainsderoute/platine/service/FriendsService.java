package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.FriendsRequestResponseDTO;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import com.univ.lille.copainsderoute.platine.exceptions.*;
import com.univ.lille.copainsderoute.platine.repository.FriendsRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendsService {

    private FriendsRepository friendsRepository;
    private UserRepository userRepository;

    public void sendFriendRequest(FriendsRequestDTOs friendsRequestDTOs, String login) throws UserNotFoundException, FriendRequestAlreadyExistsException {
        User userSending = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        User userToAdd = userRepository.findByLogin(friendsRequestDTOs.getLoginNewFriend()).orElseThrow(UserNotFoundException::new);

        Optional<Friends> existingFriendRequestSenderToAdded = friendsRepository.findBySenderAndAdded(userSending, userToAdd);
        Optional<Friends> existingFriendRequestAddedToSender = friendsRepository.findBySenderAndAdded(userToAdd, userSending);
        if(existingFriendRequestSenderToAdded.isPresent() || existingFriendRequestAddedToSender.isPresent()) {
            throw new FriendRequestAlreadyExistsException();
        }

        Friends friendRequest = new Friends();
        friendRequest.setSender(userSending);
        friendRequest.setAdded(userToAdd);
        friendRequest.setStatus(FriendRequestStatus.SENT);
        friendRequest.setSubmissionTime(LocalDateTime.now());
        friendsRepository.save(friendRequest);
    }

    public List<FriendsRequestResponseDTO> getFriendRequests(String login) throws UserNotFoundException, NoFriendRequestsException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        List<Friends> friendRequests = friendsRepository.findBySenderOrAdded(user).orElseThrow(NoFriendRequestsException::new);
        return friendRequests.stream().map(FriendsRequestResponseDTO::new).toList();
    }

    public void acceptFriendRequest(int id, String login) throws UserNotFoundException, FriendRequestNotFound, UserCanOnlyAcceptHisFriendRequestException, FriendRequestIsNotInStatusSentException {
        User userSending = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        Friends friendRequest = friendsRepository.findById(id).orElseThrow(FriendRequestNotFound::new);

        if(!friendRequest.getAdded().equals(userSending)) {
            throw new UserCanOnlyAcceptHisFriendRequestException();
        }

        if(friendRequest.getStatus().equals(FriendRequestStatus.SENT)) {
            friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
            friendRequest.setAcceptedTime(LocalDateTime.now());
            friendsRepository.save(friendRequest);
        }
        else {
            throw new FriendRequestIsNotInStatusSentException();
        }
    }

    public void denyFriendRequest(int id, String login) throws UserNotFoundException, FriendRequestNotFound, UserCanOnlyAcceptHisFriendRequestException, FriendRequestIsNotInStatusSentException {
        User userSending = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        Friends friendRequest = friendsRepository.findById(id).orElseThrow(FriendRequestNotFound::new);

        if(!friendRequest.getAdded().equals(userSending)) {
            throw new UserCanOnlyAcceptHisFriendRequestException();
        }

        if(friendRequest.getStatus().equals(FriendRequestStatus.SENT)) {
            friendRequest.setStatus(FriendRequestStatus.REFUSED);
            friendRequest.setAcceptedTime(LocalDateTime.now());
            friendsRepository.save(friendRequest);
        }
        else {
            throw new FriendRequestIsNotInStatusSentException();
        }
    }

    public void deleteFriend(int id, String login) throws UserNotFoundException, FriendRequestNotFound, UserCanDeleteOnlyHisFriendsException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        Friends friend = friendsRepository.findById(id).orElseThrow(FriendRequestNotFound::new);
        if(friend.getSender().equals(user) || friend.getAdded().equals(user)) {
            friendsRepository.delete(friend);
        }
        else {
            throw new UserCanDeleteOnlyHisFriendsException();
        }
    }
}
