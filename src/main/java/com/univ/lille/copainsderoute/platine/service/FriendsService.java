package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.repository.FriendsRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendsService {

    private FriendsRepository friendsRepository;

    private UserRepository userRepository;

    public FriendRequestStatus sendFriendRequest(FriendsRequestDTOs friendsRequestDTOs) throws UserNotFoundException {
        
        Friends friends = new Friends();

        User user1 = userRepository.findByLogin(friendsRequestDTOs.getLoginUser1()).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findByLogin(friendsRequestDTOs.getLoginUser2()).orElseThrow(UserNotFoundException::new);

        friends.setUser1(user1);
        friends.setUser2(user2);
        friends.setStatus(FriendRequestStatus.SENT);
        friends.setSubmissionTime(LocalDateTime.now());
        friendsRepository.save(friends);
        return FriendRequestStatus.SENT;
    }
}
