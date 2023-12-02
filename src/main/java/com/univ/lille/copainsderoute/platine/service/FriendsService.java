package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
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

    public FriendRequestStatus sendFriendRequest(FriendsRequestDTOs friendsRequestDTOs) throws RuntimeException{
        
        Friends friends = new Friends();

        User user1 = userRepository.findByLogin(friendsRequestDTOs.getLoginUser1());
        User user2 = userRepository.findByLogin(friendsRequestDTOs.getLoginUser2());

        if (user1 == null ) {
            throw new RuntimeException("User 1 not found");
        }

        if (user2 == null ) {

            throw new RuntimeException("User 2 not found");
        }

        friends.setUser1(user1);
        friends.setUser2(user2);
        friends.setStatus(FriendRequestStatus.SENT);
        friends.setSubmissionTime(LocalDateTime.now());
        friendsRepository.save(friends);
        return FriendRequestStatus.SENT;
    }
}
